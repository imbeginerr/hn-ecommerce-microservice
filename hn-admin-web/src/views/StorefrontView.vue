<template>
  <div class="min-h-screen bg-zinc-50">
    <StoreNavbar
      v-model:search-text="searchText"
      :search-results="searchResults"
      :cart-count="cartCount"
      :username="username"
      :avatar-text="avatarText"
      :primary-image="primaryImage"
      @home="clearFilters"
      @search="searchProducts"
      @clear-search="clearSearch"
      @select-search="applySearchResult"
      @open-cart="showCart = true"
      @open-register="showRegister = true"
      @open-account="showAccount = true"
      @logout="logout"
    />

    <main class="mx-auto grid max-w-7xl gap-7 px-5 py-7">
      <StoreBanner />

      <div class="flex justify-end lg:hidden">
        <el-button :icon="Filter" type="primary" plain @click="showFilters = true">Filter</el-button>
      </div>

      <div class="grid gap-6 lg:grid-cols-[260px_1fr]">
        <StoreFilterSidebar
          class="sticky top-24 hidden lg:grid"
          v-model:sort-mode="sortMode"
          v-model:price-range="priceRange"
          :categories="categories"
          :selected-category="selectedCategory"
          :max-price="maxPrice"
          :format-price="formatPrice"
          @select-category="selectCategory"
          @apply-price="applyPriceFilter"
        />
        <ProductList
          :products="visibleProducts"
          :loading="loading"
          :error="error"
          :primary-image="primaryImage"
          :format-price="formatPrice"
          @detail="openDetail"
          @add-cart="addToCart"
        />
      </div>
    </main>

    <el-drawer v-model="showFilters" size="min(86vw, 360px)" title="Filter products" direction="ltr">
      <StoreFilterSidebar
        v-model:sort-mode="sortMode"
        v-model:price-range="priceRange"
        :categories="categories"
        :selected-category="selectedCategory"
        :max-price="maxPrice"
        :format-price="formatPrice"
        @select-category="selectCategory"
        @apply-price="applyPriceFilter"
      />
    </el-drawer>

    <CartDrawer
      v-model="showCart"
      v-model:payment-method="paymentMethod"
      :items="cartItems"
      :total="cartTotal"
      :message="checkoutMessage"
      :checking-out="checkingOut"
      :primary-image="primaryImage"
      :format-price="formatPrice"
      @quantity-change="changeCartQuantity"
      @remove="removeFromCart"
      @checkout="checkoutCart"
    />

    <ProductDetailDialog
      v-model:quantity="checkoutQuantity"
      v-model:payment-method="paymentMethod"
      :product="selectedProduct"
      :images="detailImages"
      :current-image="currentDetailImage"
      :image-index="imageIndex"
      :safe-description="safeDescription"
      :checking-out="checkingOut"
      :message="checkoutMessage"
      :format-price="formatPrice"
      @close="selectedProduct = null"
      @previous="previousImage"
      @next="nextImage"
      @checkout="checkoutProduct"
    />

    <RegisterDialog v-model="showRegister" :form="registerForm" :loading="registering" @submit="register" />
    <AccountDrawer
      v-model="showAccount"
      :form="accountForm"
      :avatar-text="avatarText"
      :loading="loadingAccount"
      :saving="savingAccount"
      @load="loadAccount"
      @save="saveAccount"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { Filter } from '@element-plus/icons-vue';
import StoreNavbar from '../components/store/StoreNavbar.vue';
import StoreBanner from '../components/store/StoreBanner.vue';
import StoreFilterSidebar from '../components/store/StoreFilterSidebar.vue';
import ProductList from '../components/store/ProductList.vue';
import CartDrawer from '../components/store/CartDrawer.vue';
import ProductDetailDialog from '../components/store/ProductDetailDialog.vue';
import RegisterDialog from '../components/store/RegisterDialog.vue';
import AccountDrawer from '../components/store/AccountDrawer.vue';
import { getPublicProducts } from '../api/productApi';
import { createOrder } from '../api/orderApi';
import { getMyAccount, registerAccount, updateMyAccount } from '../api/accountApi';
import { clearTokens, getAccessToken } from '../utils/tokenStorage';
import { getCurrentUsername } from '../utils/authScope';
import { notifyInfo, notifySuccess } from '../utils/notify';

const router = useRouter();
const allProducts = ref([]);
const products = ref([]);
const searchResults = ref([]);
const searchText = ref('');
const selectedCategory = ref('');
const sortMode = ref('desc');
const selectedProduct = ref(null);
const imageIndex = ref(0);
const cartItems = ref([]);
const showCart = ref(false);
const showFilters = ref(false);
const showRegister = ref(false);
const showAccount = ref(false);
const loading = ref(false);
const checkingOut = ref(false);
const registering = ref(false);
const loadingAccount = ref(false);
const savingAccount = ref(false);
const error = ref('');
const checkoutMessage = ref('');
const checkoutQuantity = ref(1);
const paymentMethod = ref('CASH');
const priceRange = ref([0, 0]);
const authVersion = ref(0);
const registerForm = reactive({ username: '', fullName: '', password: '', dob: '' });
const accountForm = reactive({ username: '', fullName: '', dob: '', address: '' });

const username = computed(() => {
  authVersion.value;
  return getCurrentUsername();
});
const avatarText = computed(() => (username.value || 'U').slice(0, 2).toUpperCase());
const cartCount = computed(() => cartItems.value.reduce((total, item) => total + Number(item.quantity || 0), 0));
const cartTotal = computed(() => cartItems.value.reduce((total, item) => total + Number(item.product.price || 0) * Number(item.quantity || 0), 0));
const categories = computed(() => [...new Set(allProducts.value.map((product) => product.category).filter(Boolean))].sort());
const maxPrice = computed(() => Math.max(0, ...allProducts.value.map((product) => Number(product.price || 0))));

const visibleProducts = computed(() => {
  const sorted = [...products.value];
  sorted.sort((a, b) => {
    const left = Number(a.price || 0);
    const right = Number(b.price || 0);
    return sortMode.value === 'asc' ? left - right : right - left;
  });
  return sorted;
});

const detailImages = computed(() => {
  const images = selectedProduct.value?.images || [];
  return images.length ? images.map((image) => normalizeImageUrl(image.url)) : [fallbackImage()];
});

const currentDetailImage = computed(() => detailImages.value[imageIndex.value] || fallbackImage());

const safeDescription = computed(() => {
  const html = selectedProduct.value?.description || '<p>No product description yet.</p>';
  return html
    .replace(/<script[\s\S]*?>[\s\S]*?<\/script>/gi, '')
    .replace(/\son\w+="[^"]*"/gi, '')
    .replace(/\son\w+='[^']*'/gi, '');
});

async function loadProducts() {
  loading.value = true;
  error.value = '';
  try {
    const data = await getPublicProducts();
    products.value = data;
    allProducts.value = data;
    priceRange.value = [0, Math.max(0, ...data.map((product) => Number(product.price || 0)))];
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
}

let searchTimer;
function searchProducts() {
  clearTimeout(searchTimer);
  searchTimer = setTimeout(() => {
    const keyword = normalizeText(searchText.value);
    searchResults.value = keyword
      ? allProducts.value.filter((product) => normalizeText(product.name).includes(keyword) || normalizeText(product.category).includes(keyword)).slice(0, 8)
      : [];
  }, 180);
}

function applySearchResult(product) {
  selectedCategory.value = '';
  searchText.value = product.name;
  searchResults.value = [];
  const keyword = normalizeText(product.name);
  products.value = allProducts.value.filter((item) => normalizeText(item.name).includes(keyword));
}

function clearSearch() {
  searchText.value = '';
  searchResults.value = [];
}

function selectCategory(category) {
  selectedCategory.value = category;
  clearSearch();
  products.value = category ? allProducts.value.filter((product) => normalizeText(product.category) === normalizeText(category)) : allProducts.value;
  showFilters.value = false;
}

function applyPriceFilter() {
  const [minPrice, maxSelectedPrice] = priceRange.value;
  products.value = allProducts.value.filter((product) => {
    const price = Number(product.price || 0);
    const categoryMatched = selectedCategory.value ? normalizeText(product.category) === normalizeText(selectedCategory.value) : true;
    return categoryMatched && price >= minPrice && price <= maxSelectedPrice;
  });
  showFilters.value = false;
  notifySuccess('Price filter applied.');
}

function clearFilters() {
  selectedCategory.value = '';
  clearSearch();
  products.value = allProducts.value;
  priceRange.value = [0, maxPrice.value];
}

function openDetail(product) {
  selectedProduct.value = product;
  imageIndex.value = 0;
  checkoutQuantity.value = 1;
  checkoutMessage.value = '';
  searchResults.value = [];
}

function addToCart(product) {
  const existingItem = cartItems.value.find((item) => item.product.id === product.id);
  if (existingItem) existingItem.quantity += 1;
  else cartItems.value.push({ product, quantity: 1 });
  notifySuccess(`${product.name} added to cart.`);
}

function changeCartQuantity(productId, quantity) {
  const item = cartItems.value.find((cartItem) => cartItem.product.id === productId);
  if (item) item.quantity = Number(quantity || 1);
}

function removeFromCart(productId) {
  cartItems.value = cartItems.value.filter((item) => item.product.id !== productId);
  notifyInfo('Removed product from cart.');
}

async function checkoutProduct() {
  await createOrderFromItems([{ product: selectedProduct.value, quantity: checkoutQuantity.value }]);
}

async function checkoutCart() {
  await createOrderFromItems(cartItems.value);
  if (!checkoutMessage.value.startsWith('Please') && !checkoutMessage.value.startsWith('Cannot')) cartItems.value = [];
}

async function createOrderFromItems(items) {
  if (!getAccessToken()) {
    checkoutMessage.value = 'Please login before checkout.';
    return;
  }
  checkingOut.value = true;
  checkoutMessage.value = '';
  try {
    await createOrder({
      customerUsername: username.value,
      paymentMethod: paymentMethod.value,
      items: items.map((item) => ({ productId: item.product.id, quantity: Number(item.quantity || 1), transactionType: 'INCOME' }))
    });
    checkoutMessage.value = 'Order created successfully.';
    notifySuccess(checkoutMessage.value);
  } catch (err) {
    checkoutMessage.value = `Cannot create order: ${err.message}`;
  } finally {
    checkingOut.value = false;
  }
}

async function register() {
  registering.value = true;
  try {
    await registerAccount({ ...registerForm });
    notifySuccess('Account registered. You can login now.');
    showRegister.value = false;
    registerForm.username = '';
    registerForm.fullName = '';
    registerForm.password = '';
    registerForm.dob = '';
    router.push('/login');
  } finally {
    registering.value = false;
  }
}

async function loadAccount() {
  if (!getAccessToken()) return;
  loadingAccount.value = true;
  try {
    const account = await getMyAccount();
    accountForm.username = account.username || '';
    accountForm.fullName = account.fullName || '';
    accountForm.dob = account.dob || '';
    accountForm.address = account.address || '';
  } finally {
    loadingAccount.value = false;
  }
}

async function saveAccount() {
  savingAccount.value = true;
  try {
    await updateMyAccount({
      fullName: accountForm.fullName,
      dob: accountForm.dob,
      address: accountForm.address
    });
    notifySuccess('Account updated.');
    showAccount.value = false;
  } finally {
    savingAccount.value = false;
  }
}

function logout() {
  clearTokens();
  authVersion.value += 1;
  showAccount.value = false;
  showCart.value = false;
  checkoutMessage.value = '';
  notifySuccess('Signed out.');
  if (router.currentRoute.value.path !== '/shop') router.push('/shop');
}

function previousImage() {
  imageIndex.value = imageIndex.value === 0 ? detailImages.value.length - 1 : imageIndex.value - 1;
}

function nextImage() {
  imageIndex.value = (imageIndex.value + 1) % detailImages.value.length;
}

function primaryImage(product) {
  return normalizeImageUrl(product.images?.[0]?.url) || fallbackImage();
}

function normalizeImageUrl(url) {
  if (!url) return '';
  if (url.startsWith('http')) return url;
  return url.startsWith('/') ? url : `/${url}`;
}

function fallbackImage() {
  return 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80';
}

function formatPrice(price) {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(Number(price || 0));
}

function normalizeText(value = '') {
  return String(value).normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase().trim();
}

onMounted(loadProducts);
</script>

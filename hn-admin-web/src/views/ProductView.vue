<template>
  <section class="page-grid">
    <header class="page-header">
      <div>
        <p class="eyebrow">Catalog</p>
        <h1>Products</h1>
      </div>
      <div class="form-actions">
        <input ref="csvInput" class="hidden-file-input" accept=".csv,text/csv" type="file" @change="importCsv" />
        <button v-if="canWrite" class="secondary-button" type="button" @click="csvInput?.click()">Import CSV</button>
        <button class="secondary-button" type="button" @click="load">Refresh</button>
      </div>
    </header>

    <form v-if="canWrite" class="panel form-grid" @submit.prevent="save">
      <label>
        SKU
        <input v-model="form.sku" required />
      </label>
      <label>
        Name
        <input v-model="form.name" required />
      </label>
      <label>
        Price
        <input v-model.number="form.price" min="0" step="0.01" type="number" required />
      </label>
      <label>
        Category
        <input v-model="form.category" placeholder="Shoes, Bags, Accessories..." />
      </label>
      <label>
        Active
        <select v-model="form.active">
          <option :value="true">Active</option>
          <option :value="false">Inactive</option>
        </select>
      </label>
      <label class="wide-field">
        Description HTML
        <textarea v-model="form.description" rows="6" placeholder="<p>Product details...</p>" />
      </label>
      <label class="wide-field">
        Product images
        <input ref="imageInput" class="hidden-file-input" accept="image/*" multiple type="file" @change="handleImageChange" />
        <button class="file-button" type="button" @click="imageInput?.click()">Choose images</button>
        <div v-if="existingImages.length" class="selected-file-list image-preview-list">
          <span v-for="image in existingImages" :key="image.id || image.url" class="image-preview-chip">
            <img :src="imageUrl(image.url)" :alt="image.fileName || form.name" />
            <span>{{ image.fileName || 'Current image' }}</span>
            <button type="button" @click="removeExistingImage(image.id)">Remove</button>
          </span>
        </div>
        <div v-if="selectedImages.length" class="selected-file-list">
          <span v-for="(file, index) in selectedImages" :key="fileKey(file, index)" class="file-chip">
            {{ file.name }}
            <button type="button" @click="removeSelectedImage(index)">x</button>
          </span>
        </div>
      </label>
      <div class="form-actions">
        <button class="primary-button" type="submit">{{ editingId ? 'Update' : 'Create' }}</button>
        <button v-if="editingId" class="ghost-button" type="button" @click="reset">Cancel</button>
      </div>
      <p v-if="error" class="error-text">{{ error }}</p>
    </form>

    <div class="panel table-panel">
      <table>
        <thead>
          <tr>
            <th>SKU</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Images</th>
            <th>Status</th>
            <th v-if="canWrite"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>{{ product.sku }}</td>
            <td>{{ product.name }}</td>
            <td>{{ product.category || '-' }}</td>
            <td>{{ product.price }}</td>
            <td>
              <div class="image-strip">
                <img v-for="image in product.images" :key="image.id" :src="imageUrl(image.url)" :alt="image.fileName" />
              </div>
            </td>
            <td><span class="pill">{{ product.active ? 'Active' : 'Inactive' }}</span></td>
            <td v-if="canWrite" class="row-actions">
              <button class="text-button" type="button" @click="edit(product)">Edit</button>
              <button class="danger-button" type="button" @click="remove(product.id)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { createProduct, deleteProduct, deleteProductImage, getProducts, updateProduct, uploadProductImages } from '../api/productApi';
import { hasAnyAuthority } from '../utils/authScope';
import { notifySuccess } from '../utils/notify';

const products = ref([]);
const editingId = ref(null);
const error = ref('');
const canWrite = computed(() => hasAnyAuthority(['ROLE_ADMIN', 'ProductCreate', 'ProductUpdate', 'ProductDelete']));
const form = reactive({ sku: '', name: '', category: '', description: '', price: 0, active: true });
const selectedImages = ref([]);
const existingImages = ref([]);
const imageInput = ref(null);
const csvInput = ref(null);

async function load() {
  error.value = '';
  try {
    products.value = await getProducts();
  } catch (err) {
    error.value = err.message;
  }
}

function reset() {
  editingId.value = null;
  form.sku = '';
  form.name = '';
  form.category = '';
  form.description = '';
  form.price = 0;
  form.active = true;
  selectedImages.value = [];
  existingImages.value = [];
  if (imageInput.value) imageInput.value.value = '';
}

function edit(product) {
  editingId.value = product.id;
  form.sku = product.sku;
  form.name = product.name;
  form.category = product.category || '';
  form.description = product.description || '';
  form.price = Number(product.price || 0);
  form.active = product.active;
  existingImages.value = product.images || [];
  selectedImages.value = [];
  if (imageInput.value) imageInput.value.value = '';
}

async function save() {
  const payload = { ...form };
  const savedProduct = editingId.value ? await updateProduct(editingId.value, payload) : await createProduct(payload);
  if (selectedImages.value.length > 0) {
    await uploadProductImages(savedProduct.id, selectedImages.value);
  }
  reset();
  await load();
}

async function importCsv(event) {
  const [file] = Array.from(event.target.files || []);
  event.target.value = '';
  if (!file) return;
  
  const content = await file.text();
  const rows = parseCsv(content);
  if (rows.length < 2) return;
  
  const headers = rows[0].map((header) => header.trim());
  let imported = 0;
  for (const row of rows.slice(1)) {
    if (row.every((cell) => !cell.trim())) continue;
    const record = Object.fromEntries(headers.map((header, index) => [header, row[index] || '']));
    await createProduct({
      sku: record.sku,
      name: record.name,
      category: record.category,
      description: record.description,
      price: Number(record.price || 0),
      active: String(record.active || 'true').toLowerCase() !== 'false'
    });
    imported += 1;
  }
  notifySuccess(`Imported ${imported} products from CSV.`);
  await load();
}

function parseCsv(content) {
  const rows = [];
  let row = [];
  let cell = '';
  let quoted = false;
  
  for (let index = 0; index < content.length; index += 1) {
    const char = content[index];
    const next = content[index + 1];
    if (char === '"' && quoted && next === '"') {
      cell += '"';
      index += 1;
    } else if (char === '"') {
      quoted = !quoted;
    } else if (char === ',' && !quoted) {
      row.push(cell);
      cell = '';
    } else if ((char === '\n' || char === '\r') && !quoted) {
      if (char === '\r' && next === '\n') index += 1;
      row.push(cell);
      rows.push(row);
      row = [];
      cell = '';
    } else {
      cell += char;
    }
  }
  if (cell || row.length) {
    row.push(cell);
    rows.push(row);
  }
  return rows;
}

function handleImageChange(event) {
  const incomingFiles = Array.from(event.target.files || []);
  const currentKeys = new Set(selectedImages.value.map((file) => fileKey(file)));
  incomingFiles.forEach((file) => {
    if (!currentKeys.has(fileKey(file))) {
      selectedImages.value.push(file);
    }
  });
  event.target.value = '';
}

function removeSelectedImage(index) {
  selectedImages.value.splice(index, 1);
}

async function removeExistingImage(imageId) {
  if (!editingId.value || !imageId) return;
  const updatedProduct = await deleteProductImage(editingId.value, imageId);
  existingImages.value = updatedProduct.images || [];
  products.value = products.value.map((product) => (product.id === updatedProduct.id ? updatedProduct : product));
  notifySuccess('Product image removed.');
}

function fileKey(file, index = '') {
  return `${file.name}-${file.size}-${file.lastModified}-${index}`;
}

function imageUrl(url) {
  if (!url) return '';
  if (url.startsWith('http')) return url;
  return url.startsWith('/') ? url : `/${url}`;
}

async function remove(id) {
  if (!confirm('Delete product?')) return;
  await deleteProduct(id);
  await load();
}

onMounted(load);
</script>

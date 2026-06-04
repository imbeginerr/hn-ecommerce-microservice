<template>
  <section class="page-grid">
    <header class="page-header">
      <div>
        <p class="eyebrow">Sales</p>
        <h1>Orders</h1>
      </div>
      <button class="secondary-button" type="button" @click="loadAll">Refresh</button>
    </header>

    <form v-if="canCreate" class="panel form-grid" @submit.prevent="save">
      <label>
        Customer username
        <input v-model="form.customerUsername" />
      </label>
      <label>
        Product
        <select v-model.number="form.productId" required>
          <option disabled value="">Select product</option>
          <option v-for="product in products" :key="product.id" :value="product.id">
            {{ product.sku }} - {{ product.name }}
          </option>
        </select>
      </label>
      <label>
        Quantity
        <input v-model.number="form.quantity" min="1" type="number" required />
      </label>
      <label>
        Payment method
        <select v-model="form.paymentMethod">
          <option value="CASH">Cash</option>
          <option value="BANK_TRANSFER">Bank transfer</option>
          <option value="CARD">Card</option>
          <option value="E_WALLET">E-wallet</option>
        </select>
      </label>
      <label>
        Transaction type
        <select v-model="form.transactionType">
          <option value="INCOME">Income</option>
          <option value="EXPENSE">Expense</option>
        </select>
      </label>
      <div class="form-actions">
        <button class="primary-button" type="submit">Create order</button>
      </div>
      <p v-if="error" class="error-text">{{ error }}</p>
    </form>

    <div class="panel table-panel">
      <table>
        <thead>
          <tr>
            <th>Order</th>
            <th>Customer</th>
            <th>Payment</th>
            <th>Status</th>
            <th>Total</th>
            <th>Items</th>
            <th v-if="canWrite"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.orderNumber }}</td>
            <td>{{ order.customerUsername }}</td>
            <td>{{ order.paymentMethod }}</td>
            <td><span class="pill">{{ order.status }}</span></td>
            <td>{{ order.totalAmount }}</td>
            <td>
              <div v-for="item in order.items" :key="item.productId">
                {{ item.sku }} x {{ item.quantity }} - {{ item.transactionType }}
              </div>
            </td>
            <td v-if="canWrite" class="row-actions">
              <button class="text-button" type="button" @click="setStatus(order.id, 'CONFIRMED')">Confirm</button>
              <button class="danger-button" type="button" @click="setStatus(order.id, 'CANCELLED')">Cancel</button>
              <button class="danger-button" type="button" @click="remove(order.id)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { createOrder, deleteOrder, getOrders, updateOrderStatus } from '../api/orderApi';
import { getProducts } from '../api/productApi';
import { hasAnyAuthority } from '../utils/authScope';

const orders = ref([]);
const products = ref([]);
const error = ref('');
const canCreate = computed(() => hasAnyAuthority(['ROLE_ADMIN', 'OrderCreate']));
const canWrite = computed(() => hasAnyAuthority(['ROLE_ADMIN', 'OrderUpdate', 'OrderDelete']));
const form = reactive({ customerUsername: '', productId: '', quantity: 1, paymentMethod: 'CASH', transactionType: 'INCOME' });

async function loadAll() {
  error.value = '';
  try {
    const [orderData, productData] = await Promise.all([getOrders(), getProducts()]);
    orders.value = orderData;
    products.value = productData;
  } catch (err) {
    error.value = err.message;
  }
}

async function save() {
  await createOrder({
    customerUsername: form.customerUsername,
    paymentMethod: form.paymentMethod,
    items: [{ productId: form.productId, quantity: form.quantity, transactionType: form.transactionType }]
  });
  form.customerUsername = '';
  form.productId = '';
  form.quantity = 1;
  form.paymentMethod = 'CASH';
  form.transactionType = 'INCOME';
  await loadAll();
}

async function setStatus(id, status) {
  await updateOrderStatus(id, status);
  await loadAll();
}

async function remove(id) {
  if (!confirm('Delete order?')) return;
  await deleteOrder(id);
  await loadAll();
}

onMounted(loadAll);
</script>

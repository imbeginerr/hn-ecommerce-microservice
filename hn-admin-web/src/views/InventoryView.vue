<template>
  <section class="page-grid">
    <header class="page-header">
      <div>
        <p class="eyebrow">Operations</p>
        <h1>Inventory</h1>
      </div>
      <button class="secondary-button" type="button" @click="load">Refresh</button>
    </header>

    <form v-if="canWrite" class="panel form-grid" @submit.prevent="save">
      <label>
        Product ID
        <input v-model.number="form.productId" min="1" type="number" required />
      </label>
      <label>
        SKU
        <input v-model="form.sku" required />
      </label>
      <label>
        Quantity
        <input v-model.number="form.quantity" min="0" type="number" required />
      </label>
      <label>
        Reserved
        <input v-model.number="form.reservedQuantity" min="0" type="number" />
      </label>
      <label class="wide-field">
        Warehouse
        <input v-model="form.warehouseLocation" />
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
            <th>Product ID</th>
            <th>SKU</th>
            <th>Quantity</th>
            <th>Reserved</th>
            <th>Available</th>
            <th>Warehouse</th>
            <th v-if="canWrite"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in inventories" :key="item.id">
            <td>{{ item.productId }}</td>
            <td>{{ item.sku }}</td>
            <td>{{ item.quantity }}</td>
            <td>{{ item.reservedQuantity }}</td>
            <td><span class="pill">{{ item.availableQuantity }}</span></td>
            <td>{{ item.warehouseLocation }}</td>
            <td v-if="canWrite" class="row-actions">
              <button class="text-button" type="button" @click="edit(item)">Edit</button>
              <button class="danger-button" type="button" @click="remove(item.id)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { createInventory, deleteInventory, getInventories, updateInventory } from '../api/inventoryApi';
import { hasAnyAuthority } from '../utils/authScope';

const inventories = ref([]);
const editingId = ref(null);
const error = ref('');
const canWrite = computed(() => hasAnyAuthority(['ROLE_ADMIN', 'InventoryCreate', 'InventoryUpdate', 'InventoryDelete']));
const form = reactive({ productId: null, sku: '', quantity: 0, reservedQuantity: 0, warehouseLocation: '' });

async function load() {
  error.value = '';
  try {
    inventories.value = await getInventories();
  } catch (err) {
    error.value = err.message;
  }
}

function reset() {
  editingId.value = null;
  form.productId = null;
  form.sku = '';
  form.quantity = 0;
  form.reservedQuantity = 0;
  form.warehouseLocation = '';
}

function edit(item) {
  editingId.value = item.id;
  form.productId = item.productId;
  form.sku = item.sku;
  form.quantity = item.quantity;
  form.reservedQuantity = item.reservedQuantity;
  form.warehouseLocation = item.warehouseLocation || '';
}

async function save() {
  const payload = { ...form };
  if (editingId.value) await updateInventory(editingId.value, payload);
  else await createInventory(payload);
  reset();
  await load();
}

async function remove(id) {
  if (!confirm('Delete inventory item?')) return;
  await deleteInventory(id);
  await load();
}

onMounted(load);
</script>

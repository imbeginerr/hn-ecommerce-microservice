<template>
  <section class="page-grid">
    <header class="page-header">
      <div>
        <p class="eyebrow">Authorization</p>
        <h1>Permissions</h1>
      </div>
      <button class="secondary-button" type="button" @click="load">Refresh</button>
    </header>

    <form class="panel form-grid" @submit.prevent="save">
      <label>
        Name
        <input v-model="form.name" :disabled="isEditing" required />
      </label>
      <label>
        Description
        <input v-model="form.description" />
      </label>
      <div class="form-actions">
        <button class="primary-button" type="submit">{{ isEditing ? 'Update' : 'Create' }}</button>
        <button v-if="isEditing" class="ghost-button" type="button" @click="reset">Cancel</button>
      </div>
      <p v-if="error" class="error-text">{{ error }}</p>
    </form>

    <div class="panel table-panel">
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="permission in permissions" :key="permission.name">
            <td>{{ permission.name }}</td>
            <td>{{ permission.description }}</td>
            <td class="row-actions">
              <button class="text-button" type="button" @click="edit(permission)">Edit</button>
              <button class="danger-button" type="button" @click="remove(permission.name)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { createPermission, deletePermission, getPermissions, updatePermission } from '../api/permissionApi';

const permissions = ref([]);
const editingName = ref('');
const error = ref('');
const form = reactive({ name: '', description: '' });
const isEditing = computed(() => Boolean(editingName.value));

function normalizePage(page) {
  return page?.content || [];
}

async function load() {
  error.value = '';
  try {
    permissions.value = normalizePage(await getPermissions());
  } catch (err) {
    error.value = err.message;
  }
}

function reset() {
  editingName.value = '';
  form.name = '';
  form.description = '';
}

function edit(permission) {
  editingName.value = permission.name;
  form.name = permission.name;
  form.description = permission.description || '';
}

async function save() {
  error.value = '';
  try {
    const payload = { name: form.name, description: form.description };
    if (editingName.value) {
      await updatePermission(editingName.value, payload);
    } else {
      await createPermission(payload);
    }
    reset();
    await load();
  } catch (err) {
    error.value = err.message;
  }
}

async function remove(name) {
  if (!confirm(`Delete permission ${name}?`)) return;
  await deletePermission(name);
  await load();
}

onMounted(load);
</script>

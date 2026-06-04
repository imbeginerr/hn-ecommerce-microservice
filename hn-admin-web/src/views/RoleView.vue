<template>
  <section class="page-grid">
    <header class="page-header">
      <div>
        <p class="eyebrow">Authorization</p>
        <h1>Roles</h1>
      </div>
      <button class="secondary-button" type="button" @click="loadAll">Refresh</button>
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
      <label class="wide-field">
        Permissions
        <select v-model="form.permissions" multiple>
          <option v-for="permission in permissions" :key="permission.name" :value="permission.name">
            {{ permission.name }}
          </option>
        </select>
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
            <th>Permissions</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="role in roles" :key="role.name">
            <td>{{ role.name }}</td>
            <td>{{ role.description }}</td>
            <td>
              <div class="pill-row">
                <span v-for="permission in role.permissions" :key="permission.name" class="pill">
                  {{ permission.name }}
                </span>
              </div>
            </td>
            <td class="row-actions">
              <button class="text-button" type="button" @click="edit(role)">Edit</button>
              <button class="danger-button" type="button" @click="remove(role.name)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { getPermissions } from '../api/permissionApi';
import { createRole, deleteRole, getRoles, updateRole } from '../api/roleApi';

const roles = ref([]);
const permissions = ref([]);
const editingName = ref('');
const error = ref('');
const form = reactive({ name: '', description: '', permissions: [] });
const isEditing = computed(() => Boolean(editingName.value));

function normalizePage(page) {
  return page?.content || [];
}

async function loadAll() {
  error.value = '';
  try {
    const [rolePage, permissionPage] = await Promise.all([getRoles(), getPermissions()]);
    roles.value = normalizePage(rolePage);
    permissions.value = normalizePage(permissionPage);
  } catch (err) {
    error.value = err.message;
  }
}

function reset() {
  editingName.value = '';
  form.name = '';
  form.description = '';
  form.permissions = [];
}

function edit(role) {
  editingName.value = role.name;
  form.name = role.name;
  form.description = role.description || '';
  form.permissions = (role.permissions || []).map((permission) => permission.name);
}

async function save() {
  error.value = '';
  try {
    const payload = {
      name: form.name,
      description: form.description,
      permissions: form.permissions
    };
    if (editingName.value) {
      await updateRole(editingName.value, payload);
    } else {
      await createRole(payload);
    }
    reset();
    await loadAll();
  } catch (err) {
    error.value = err.message;
  }
}

async function remove(name) {
  if (!confirm(`Delete role ${name}?`)) return;
  await deleteRole(name);
  await loadAll();
}

onMounted(loadAll);
</script>

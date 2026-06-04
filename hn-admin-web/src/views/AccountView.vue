<template>
  <section class="page-grid">
    <header class="page-header">
      <div>
        <p class="eyebrow">Identity</p>
        <h1>Accounts</h1>
      </div>
      <button class="secondary-button" type="button" @click="loadAll">Refresh</button>
    </header>

    <form class="panel form-grid" @submit.prevent="save">
      <label>
        Username
        <input v-model="form.username" :disabled="isEditing" required />
      </label>
      <label>
        Password
        <input v-model="form.password" type="password" :required="!isEditing" />
      </label>
      <label>
        Full name
        <input v-model="form.fullName" required />
      </label>
      <label>
        Date of birth
        <div class="date-select-row">
          <select v-model="dob.day">
            <option value="">Day</option>
            <option v-for="day in days" :key="day" :value="day">{{ day }}</option>
          </select>
          <select v-model="dob.month">
            <option value="">Month</option>
            <option v-for="month in months" :key="month" :value="month">{{ month }}</option>
          </select>
          <select v-model="dob.year">
            <option value="">Year</option>
            <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
          </select>
        </div>
      </label>
      <label class="wide-field">
        Roles
        <select v-model="form.roles" multiple>
          <option v-for="role in roles" :key="role.name" :value="role.name">
            {{ role.name }}
          </option>
        </select>
      </label>
      <label class="wide-field">
        Permissions from selected roles
        <select :value="selectedPermissionNames" multiple disabled>
          <option v-for="permission in selectedPermissions" :key="permission.name" :value="permission.name">
            {{ permission.name }}
          </option>
        </select>
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
            <th>Username</th>
            <th>Full name</th>
            <th>Roles</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="account in accounts" :key="account.username">
            <td>{{ account.username }}</td>
            <td>{{ account.fullName }}</td>
            <td>
              <div class="pill-row">
                <span v-for="role in account.roles" :key="role" class="pill">{{ role }}</span>
              </div>
            </td>
            <td class="row-actions">
              <button class="text-button" type="button" @click="edit(account)">Edit</button>
              <button class="danger-button" type="button" :disabled="!account.id" @click="remove(account)">
                Delete
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { createAccount, deleteAccount, getAccounts, updateAccount } from '../api/accountApi';
import { getRoles } from '../api/roleApi';

const accounts = ref([]);
const roles = ref([]);
const editingId = ref(null);
const editingUsername = ref('');
const error = ref('');
const form = reactive({
  username: '',
  password: '',
  fullName: '',
  roles: []
});
const dob = reactive({ day: '', month: '', year: '' });

const selectedRoles = computed(() => roles.value.filter((role) => form.roles.includes(role.name)));
const isEditing = computed(() => Boolean(editingId.value));
const selectedPermissions = computed(() => {
  const permissions = new Map();
  selectedRoles.value.forEach((role) => {
    (role.permissions || []).forEach((permission) => permissions.set(permission.name, permission));
  });
  return Array.from(permissions.values()).sort((left, right) => left.name.localeCompare(right.name));
});
const selectedPermissionNames = computed(() => selectedPermissions.value.map((permission) => permission.name));
const days = computed(() => Array.from({ length: 31 }, (_, index) => String(index + 1).padStart(2, '0')));
const months = computed(() => Array.from({ length: 12 }, (_, index) => String(index + 1).padStart(2, '0')));
const years = computed(() => {
  const currentYear = new Date().getFullYear();
  return Array.from({ length: 90 }, (_, index) => String(currentYear - index));
});

function normalizePage(page) {
  return page?.content || [];
}

async function loadAll() {
  error.value = '';
  try {
    const [accountPage, rolePage] = await Promise.all([getAccounts(), getRoles()]);
    accounts.value = normalizePage(accountPage);
    roles.value = normalizePage(rolePage);
  } catch (err) {
    error.value = err.message;
  }
}

function reset() {
  editingId.value = null;
  editingUsername.value = '';
  form.username = '';
  form.password = '';
  form.fullName = '';
  form.roles = [];
  dob.day = '';
  dob.month = '';
  dob.year = '';
}

function edit(account) {
  editingId.value = account.id;
  editingUsername.value = account.username;
  form.username = account.username;
  form.password = '';
  form.fullName = account.fullName || '';
  form.roles = account.roles || [];
  setDobFromText(account.dob);
}

async function save() {
  error.value = '';
  try {
    const payload = {
      username: form.username,
      fullName: form.fullName,
      dob: buildDobText(),
      roles: form.roles
    };
    if (form.password) {
      payload.password = form.password;
    }

    if (editingId.value) {
      await updateAccount(editingId.value, payload);
    } else {
      await createAccount(payload);
    }
    reset();
    await loadAll();
  } catch (err) {
    error.value = err.message;
  }
}

async function remove(account) {
  if (!account.id || !confirm(`Delete account ${account.username}?`)) return;
  await deleteAccount(account.id);
  await loadAll();
}

onMounted(loadAll);

function buildDobText() {
  if (!dob.day || !dob.month || !dob.year) {
    return null;
  }
  return `${dob.day}/${dob.month}/${dob.year}`;
}

function setDobFromText(value) {
  if (!value) {
    dob.day = '';
    dob.month = '';
    dob.year = '';
    return;
  }

  const [day, month, year] = value.split('/');
  dob.day = day || '';
  dob.month = month || '';
  dob.year = year || '';
}
</script>

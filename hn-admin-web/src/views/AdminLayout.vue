<template>
  <div class="admin-shell">
    <aside class="sidebar">
      <div class="brand">
        <span>HN</span>
        <strong>Admin</strong>
      </div>

      <nav class="nav-list">
        <RouterLink v-for="item in visibleNavItems" :key="item.to" :to="item.to">
          {{ item.label }}
        </RouterLink>
        <RouterLink v-if="visibleNavItems.length === 0" to="/no-access">No Access</RouterLink>
      </nav>

      <button class="ghost-button" type="button" @click="logout">Sign out</button>
    </aside>

    <main class="content-shell">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { computed } from 'vue';
import { hasAnyAuthority, isAdmin } from '../utils/authScope';
import { clearTokens } from '../utils/tokenStorage';

const router = useRouter();
const navItems = [
  { to: '/accounts', label: 'Accounts', authorities: ['CoreUserList'] },
  { to: '/roles', label: 'Roles', authorities: ['CoreRoleList'] },
  { to: '/permissions', label: 'Permissions', authorities: ['CorePermissionList'] },
  { to: '/products', label: 'Products', authorities: ['ProductList', 'ProductCreate', 'ProductUpdate', 'ProductDelete'] },
  {
    to: '/inventories',
    label: 'Inventory',
    authorities: ['InventoryList', 'InventoryCreate', 'InventoryUpdate', 'InventoryDelete']
  },
  { to: '/orders', label: 'Orders', authorities: ['OrderList', 'OrderCreate', 'OrderUpdate', 'OrderDelete'] }
];
const visibleNavItems = computed(() => navItems.filter((item) => isAdmin() || hasAnyAuthority(item.authorities)));

function logout() {
  clearTokens();
  router.push('/login');
}
</script>

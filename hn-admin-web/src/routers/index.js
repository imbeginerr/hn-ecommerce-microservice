import { createRouter, createWebHistory } from 'vue-router';
import { getAccessToken } from '../utils/tokenStorage';
import LoginView from '../views/LoginView.vue';
import AdminLayout from '../views/AdminLayout.vue';
import AccountView from '../views/AccountView.vue';
import InventoryView from '../views/InventoryView.vue';
import OrderView from '../views/OrderView.vue';
import ProductView from '../views/ProductView.vue';
import NoAccessView from '../views/NoAccessView.vue';
import RoleView from '../views/RoleView.vue';
import PermissionView from '../views/PermissionView.vue';
import StorefrontView from '../views/StorefrontView.vue';
import { getHomePath, hasAnyAuthority, isAdmin } from '../utils/authScope';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: LoginView },
    { path: '/shop', name: 'shop', component: StorefrontView },
    {
      path: '/',
      component: AdminLayout,
      meta: { requiresAuth: true },
      children: [
        { path: '', redirect: () => getHomePath() },
        { path: 'accounts', name: 'accounts', component: AccountView, meta: { authorities: ['CoreUserList', 'CoreUserCreate', 'CoreUserUpdate', 'CoreUserDelete'] } },
        { path: 'roles', name: 'roles', component: RoleView, meta: { authorities: ['CoreRoleList', 'CoreRoleCreate', 'CoreRoleUpdate', 'CoreRoleDelete'] } },
        { path: 'permissions', name: 'permissions', component: PermissionView, meta: { authorities: ['CorePermissionList', 'CorePermissionCreate', 'CorePermissionUpdate', 'CorePermissionDelete'] } },
        { path: 'products', name: 'products', component: ProductView, meta: { authorities: ['ProductList', 'ProductCreate', 'ProductUpdate', 'ProductDelete'] } },
        { path: 'inventories', name: 'inventories', component: InventoryView, meta: { authorities: ['InventoryList', 'InventoryCreate', 'InventoryUpdate', 'InventoryDelete'] } },
        { path: 'orders', name: 'orders', component: OrderView, meta: { authorities: ['OrderList', 'OrderCreate', 'OrderUpdate', 'OrderDelete'] } },
        { path: 'no-access', name: 'no-access', component: NoAccessView }
      ]
    }
  ]
});

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !getAccessToken()) {
    return '/login';
  }
  if (to.name === 'login' && getAccessToken()) {
    return getHomePath();
  }
  if (to.meta.authorities && !isAdmin() && !hasAnyAuthority(to.meta.authorities)) {
    return getHomePath();
  }
  return true;
});

export default router;

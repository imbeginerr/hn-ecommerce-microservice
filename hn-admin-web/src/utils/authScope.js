import { getAccessToken } from './tokenStorage';

function decodePayload(token) {
  try {
    const [, payload] = token.split('.');
    return JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
  } catch (_) {
    return {};
  }
}

export function getCurrentUsername() {
  const token = getAccessToken();
  if (!token) return '';
  return decodePayload(token).sub || '';
}

export function getAuthorities() {
  const token = getAccessToken();
  if (!token) return [];
  const payload = decodePayload(token);
  return (payload.scope || '').split(' ').filter(Boolean);
}

export function hasAuthority(authority) {
  return getAuthorities().includes(authority);
}

export function hasAnyAuthority(authorities) {
  const currentAuthorities = getAuthorities();
  return authorities.some((authority) => currentAuthorities.includes(authority));
}

export function isAdmin() {
  return hasAuthority('ROLE_ADMIN');
}

export function getHomePath() {
  if (isAdmin() || hasAnyAuthority(['CoreUserList', 'CoreRoleList', 'CorePermissionList'])) return '/accounts';
  if (hasAnyAuthority(['ProductList', 'ProductCreate', 'ProductUpdate', 'ProductDelete'])) return '/products';
  if (hasAnyAuthority(['InventoryList', 'InventoryCreate', 'InventoryUpdate', 'InventoryDelete'])) return '/inventories';
  if (hasAnyAuthority(['OrderList', 'OrderCreate', 'OrderUpdate', 'OrderDelete'])) return '/orders';
  return '/shop';
}

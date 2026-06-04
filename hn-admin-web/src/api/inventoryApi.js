import { request } from './httpClient';

export function getInventories() {
  return request('/inventory/inventories');
}

export function createInventory(payload) {
  return request('/inventory/inventories', {
    method: 'POST',
    body: payload
  });
}

export function updateInventory(id, payload) {
  return request(`/inventory/inventories/${id}`, {
    method: 'PUT',
    body: payload
  });
}

export function deleteInventory(id) {
  return request(`/inventory/inventories/${id}`, {
    method: 'DELETE'
  });
}

export function reserveInventory(payload) {
  return request('/inventory/inventories/reserve', {
    method: 'POST',
    body: payload
  });
}

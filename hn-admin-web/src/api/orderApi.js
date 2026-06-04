import { request } from './httpClient';

export function getOrders() {
  return request('/order/orders');
}

export function createOrder(payload) {
  return request('/order/orders', {
    method: 'POST',
    body: payload
  });
}

export function updateOrderStatus(id, status) {
  return request(`/order/orders/${id}/status`, {
    method: 'PUT',
    body: { status }
  });
}

export function deleteOrder(id) {
  return request(`/order/orders/${id}`, {
    method: 'DELETE'
  });
}

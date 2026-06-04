import { request, withQuery } from './httpClient';

export function getAccounts(params = {}) {
  return request(withQuery('/core/user', { page: 0, size: 100, sortBy: 'id', sortDir: 'DESC', ...params }));
}

export function createAccount(payload) {
  return request('/core/user/admin', {
    method: 'POST',
    body: payload
  });
}

export function registerAccount(payload) {
  return request('/core/user', {
    method: 'POST',
    body: payload
  });
}

export function getMyAccount() {
  return request('/core/user/myifo');
}

export function updateMyAccount(payload) {
  return request('/core/user/myifo', {
    method: 'PUT',
    body: payload
  });
}

export function updateAccount(id, payload) {
  return request(`/core/user/${id}`, {
    method: 'PUT',
    body: payload
  });
}

export function deleteAccount(id) {
  return request(`/core/user/${id}`, {
    method: 'DELETE'
  });
}

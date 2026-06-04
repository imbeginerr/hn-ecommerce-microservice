import { request, withQuery } from './httpClient';

export function getPermissions(params = {}) {
  return request(withQuery('/core/permissions', { page: 0, size: 100, sortBy: 'name', sortDir: 'ASC', ...params }));
}

export function createPermission(payload) {
  return request('/core/permissions', {
    method: 'POST',
    body: payload
  });
}

export function updatePermission(name, payload) {
  return request(`/core/permissions/${encodeURIComponent(name)}`, {
    method: 'PUT',
    body: payload
  });
}

export function deletePermission(name) {
  return request(`/core/permissions/${encodeURIComponent(name)}`, {
    method: 'DELETE'
  });
}

import { request, withQuery } from './httpClient';

export function getRoles(params = {}) {
  return request(withQuery('/core/roles', { page: 0, size: 100, sortBy: 'name', sortDir: 'ASC', ...params }));
}

export function createRole(payload) {
  return request('/core/roles', {
    method: 'POST',
    body: payload
  });
}

export function updateRole(name, payload) {
  return request(`/core/roles/${encodeURIComponent(name)}`, {
    method: 'PUT',
    body: payload
  });
}

export function deleteRole(name) {
  return request(`/core/roles/${encodeURIComponent(name)}`, {
    method: 'DELETE'
  });
}

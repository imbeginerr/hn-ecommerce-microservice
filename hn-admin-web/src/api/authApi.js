import { request } from './httpClient';

export function login(payload) {
  return request('/core/auth/token', {
    method: 'POST',
    body: payload
  });
}

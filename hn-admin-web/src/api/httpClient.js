import { clearTokens, getAccessToken } from '../utils/tokenStorage';
import { notifyError } from '../utils/notify';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api/v1';

function buildQuery(params = {}) {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      query.append(key, value);
    }
  });
  const queryString = query.toString();
  return queryString ? `?${queryString}` : '';
}

export async function request(path, options = {}) {
	const headers = new Headers(options.headers || {});
	headers.set('Content-Type', 'application/json');

  const token = getAccessToken();
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }

	let response;
	try {
		response = await fetch(`${API_BASE_URL}${path}`, {
			...options,
			headers,
			body: options.body ? JSON.stringify(options.body) : undefined
		});
	} catch (_) {
    const message = `Cannot connect to backend at ${API_BASE_URL}`;
    notifyError(message);
		throw new Error(message);
	}

  if (response.status === 401) {
    clearTokens();
  }

  const payload = response.status === 204 ? null : await response.json();
  if (!response.ok) {
    const message = payload?.message || 'Request failed';
    notifyError(message);
    throw new Error(message);
  }
  return payload?.data ?? payload;
}

export async function requestForm(path, formData, options = {}) {
  const headers = new Headers(options.headers || {});
  const token = getAccessToken();
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  let response;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      ...options,
      method: options.method || 'POST',
      headers,
      body: formData
    });
  } catch (_) {
    const message = `Cannot connect to backend at ${API_BASE_URL}`;
    notifyError(message);
    throw new Error(message);
  }

  if (response.status === 401) {
    clearTokens();
  }

  const payload = response.status === 204 ? null : await response.json();
  if (!response.ok) {
    const message = payload?.message || 'Request failed';
    notifyError(message);
    throw new Error(message);
  }
  return payload?.data ?? payload;
}

export function withQuery(path, params) {
  return `${path}${buildQuery(params)}`;
}

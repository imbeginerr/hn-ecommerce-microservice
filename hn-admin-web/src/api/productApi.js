import { request, requestForm, withQuery } from './httpClient';

export function getProducts() {
  return request('/product/products');
}

export function getPublicProducts(params = {}) {
  return request(withQuery('/product/products/public', params));
}

export function getPublicProduct(id) {
  return request(`/product/products/public/${id}`);
}

export function createProduct(payload) {
  return request('/product/products', {
    method: 'POST',
    body: payload
  });
}

export function updateProduct(id, payload) {
  return request(`/product/products/${id}`, {
    method: 'PUT',
    body: payload
  });
}

export function deleteProduct(id) {
  return request(`/product/products/${id}`, {
    method: 'DELETE'
  });
}

export function uploadProductImages(id, files) {
  const formData = new FormData();
  Array.from(files).forEach((file) => formData.append('images', file));
  return requestForm(`/product/products/${id}/images`, formData);
}

export function deleteProductImage(productId, imageId) {
  return request(`/product/products/${productId}/images/${imageId}`, {
    method: 'DELETE'
  });
}

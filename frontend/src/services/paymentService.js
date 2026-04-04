import { request } from './http'

export function payOrder(orderId) {
  return request('/payments', { method: 'POST', body: JSON.stringify({ orderId }) })
}

export function createCheckoutSession(orderId, successUrl, cancelUrl) {
  return request('/payments/checkout-session', {
    method: 'POST',
    body: JSON.stringify({ orderId, successUrl, cancelUrl })
  })
}

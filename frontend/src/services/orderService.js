import { useEffect, useState } from 'react'
import { request } from './http'

export function useOrders() {
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    request('/orders')
      .then(setOrders)
      .catch(() => setError('Unable to load orders from backend.'))
      .finally(() => setLoading(false))
  }, [])

  return { orders, loading, error }
}

export function getOrderById(id) {
  return request(`/orders/${id}`)
}

export function getCloneDraft(id) {
  return request(`/orders/${id}/clone`)
}

export function createOrder(payload) {
  return request('/orders', { method: 'POST', body: JSON.stringify(payload) })
}

export function completeOrder(orderId) {
  return request(`/orders/${orderId}/complete`, { method: 'POST' })
}

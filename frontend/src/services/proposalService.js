import { request } from './http'

export function createProposal(payload) {
  return request('/proposals', { method: 'POST', body: JSON.stringify(payload) })
}

export function getProposalsByOrder(orderId) {
  return request(`/proposals/order/${orderId}`)
}

export function acceptProposal(orderId, proposalId) {
  return request(`/orders/${orderId}/accept/${proposalId}`, { method: 'POST' })
}

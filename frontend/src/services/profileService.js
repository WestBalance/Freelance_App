import { request } from './http'

export function getFreelancerProfile(userId) {
  return request(`/profiles/freelancer/${userId}`)
}

export function saveFreelancerProfile(payload) {
  return request('/profiles/freelancer', {
    method: 'PUT',
    body: JSON.stringify(payload)
  })
}

export function getClientProfile(userId) {
  return request(`/profiles/client/${userId}`)
}

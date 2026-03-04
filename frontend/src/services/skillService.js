import { request } from './http'

export function getSkills() {
  return request('/skills')
}

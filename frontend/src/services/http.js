const API = 'http://localhost:8080/api'

export async function request(path, options = {}) {
  const response = await fetch(`${API}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    ...options
  })

  if (!response.ok) {
    const raw = await response.text()
    let parsed
    try {
      parsed = raw ? JSON.parse(raw) : null
    } catch {
      parsed = null
    }

    const error = new Error(parsed?.message || raw || `Request failed with status ${response.status}`)
    error.status = response.status
    error.responseBody = parsed ?? raw
    throw error
  }

  return response.json()
}

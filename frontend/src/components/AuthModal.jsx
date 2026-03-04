import { useEffect, useState } from 'react'

export default function AuthModal({ isOpen, onClose, mode, onSubmit }) {
  const [activeMode, setActiveMode] = useState(mode ?? 'login')
  const [form, setForm] = useState({ email: '', password: '', role: 'CLIENT' })
  const [error, setError] = useState('')

  useEffect(() => {
    if (mode) setActiveMode(mode)
  }, [mode])

  if (!isOpen) return null

  const submit = async e => {
    e.preventDefault()
    setError('')
    try {
      await onSubmit(activeMode, form)
      setForm({ email: '', password: '', role: 'CLIENT' })
      onClose()
    } catch {
      setError(activeMode === 'login' ? 'Login failed' : 'Registration failed')
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="tabs">
          <button className={activeMode === 'login' ? 'tab-btn active' : 'tab-btn'} onClick={() => setActiveMode('login')}>Login</button>
          <button className={activeMode === 'register' ? 'tab-btn active' : 'tab-btn'} onClick={() => setActiveMode('register')}>Register</button>
        </div>

        <form className="auth-form" onSubmit={submit}>
          <label>
            Email
            <input
              type="email"
              value={form.email}
              onChange={e => setForm(prev => ({ ...prev, email: e.target.value }))}
              required
            />
          </label>
          <label>
            Password
            <input
              type="password"
              value={form.password}
              onChange={e => setForm(prev => ({ ...prev, password: e.target.value }))}
              required
            />
          </label>
          {activeMode === 'register' && (
            <label>
              Role
              <select value={form.role} onChange={e => setForm(prev => ({ ...prev, role: e.target.value }))}>
                <option value="CLIENT">Client</option>
                <option value="FREELANCER">Freelancer</option>
              </select>
            </label>
          )}

          <button className="primary-btn" type="submit">{activeMode === 'login' ? 'Login' : 'Register'}</button>
          {error && <p className="error-text">{error}</p>}
        </form>
        <button className="close-link" onClick={onClose}>Close</button>
      </div>
    </div>
  )
}

import { useEffect, useState } from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import Navbar from './components/Navbar'
import Footer from './components/Footer'
import AuthModal from './components/AuthModal'
import HomePage from './pages/HomePage'
import CreateOrderPage from './pages/CreateOrderPage'
import OrderDetailsPage from './pages/OrderDetailsPage'
import ProfilePage from './pages/ProfilePage'
import { login, logout, me, register } from './services/authService'

export default function App() {
  const [isAuthOpen, setIsAuthOpen] = useState(false)
  const [authMode, setAuthMode] = useState('login')
  const [user, setUser] = useState(null)

  useEffect(() => {
    me().then(setUser).catch(() => setUser(null))
  }, [])

  const openAuth = mode => {
    setAuthMode(mode)
    setIsAuthOpen(true)
  }

  const handleAuthSubmit = async (mode, form) => {
    if (mode === 'register') {
      await register({ email: form.email, password: form.password, role: form.role })
    }
    await login({ email: form.email, password: form.password })
    const current = await me()
    setUser(current)
  }

  const handleLogout = async () => {
    await logout()
    setUser(null)
  }

  return (
    <div className="app-shell">
      <Navbar
        onOpenAuth={openAuth}
        user={user}
        onLogout={handleLogout}
      />
      <main className="app-main">
        <Routes>
          <Route path="/" element={<HomePage user={user} />} />
          <Route path="/create" element={<CreateOrderPage user={user} />} />
          <Route path="/orders/:id" element={<OrderDetailsPage user={user} />} />
          <Route path="/profile" element={<ProfilePage user={user} />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      <Footer />
      <AuthModal
        isOpen={isAuthOpen}
        onClose={() => setIsAuthOpen(false)}
        mode={authMode}
        onSubmit={handleAuthSubmit}
      />
    </div>
  )
}

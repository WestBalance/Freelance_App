import { Link, NavLink } from 'react-router-dom'

export default function Navbar({ onOpenAuth, user, onLogout }) {
  const role = user?.role

  return (
    <header className="navbar">
      <Link to="/" className="logo">TaskLink</Link>
      <nav className="nav-links">
        <NavLink to="/">Orders</NavLink>
        {role === 'CLIENT' && <NavLink to="/create">Create Order</NavLink>}
        {user && <NavLink to="/profile">My Profile</NavLink>}
      </nav>
      <div className="nav-actions">
        {!user ? (
          <>
            <button className="secondary-btn" onClick={() => onOpenAuth('login')}>Login</button>
            <button className="primary-btn" onClick={() => onOpenAuth('register')}>Register</button>
          </>
        ) : (
          <>
            <span className="user-pill">{user.email} ({user.role})</span>
            <button className="secondary-btn" onClick={onLogout}>Logout</button>
          </>
        )}
      </div>
    </header>
  )
}

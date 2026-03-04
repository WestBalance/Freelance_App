import { Link } from 'react-router-dom'

export default function OrderCard({ order, user }) {
  const canApply = user?.role === 'FREELANCER'
  const canClone = user?.role === 'CLIENT' && user?.id === order.clientId

  return (
    <article className="order-card">
      <div className="order-top">
        <h3>{order.title}</h3>
        <span className="badge">{order.category}</span>
      </div>
      <p>{order.description}</p>
      <div className="order-meta">
        <span>Budget: ${order.budget}</span>
        <span>Deadline: {order.deadline}</span>
        <span>Minimum rating: {order.minRating}</span>
        <span>Status: {order.status}</span>
      </div>
      <div className="order-actions">
        <Link className="secondary-btn" to={`/orders/${order.id}`}>View</Link>
        {canApply && <Link className="primary-btn" to={`/orders/${order.id}`}>Apply</Link>}
        {canClone && <Link className="secondary-btn" to={`/create?cloneFrom=${order.id}`}>Clone</Link>}
      </div>
    </article>
  )
}

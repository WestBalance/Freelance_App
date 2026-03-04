import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getOrderById } from '../services/orderService'
import { createProposal, getProposalsByOrder } from '../services/proposalService'

export default function OrderDetailsPage({ user }) {
  const { id } = useParams()
  const [order, setOrder] = useState(null)
  const [proposals, setProposals] = useState([])
  const [price, setPrice] = useState('')
  const [message, setMessage] = useState('')
  const [statusMessage, setStatusMessage] = useState('')

  const load = async () => {
    const [orderData, proposalData] = await Promise.all([getOrderById(id), getProposalsByOrder(id)])
    setOrder(orderData)
    setProposals(proposalData)
  }

  useEffect(() => {
    load().catch(() => setOrder(undefined))
  }, [id])

  if (order === null) return <p className="panel">Loading order...</p>
  if (order === undefined) return <p className="panel error-text">Order not found.</p>

  const canApply = user?.role === 'FREELANCER' && order.status === 'OPEN'
  const isOrderOwner = user?.role === 'CLIENT' && user.id === order.clientId

  const submitProposal = async e => {
    e.preventDefault()
    try {
      await createProposal({ orderId: order.id, price: Number(price), message, attachments: [] })
      setStatusMessage('Proposal sent.')
      setPrice('')
      setMessage('')
      await load()
    } catch (error) {
      console.error('Send proposal failed', {
        status: error?.status,
        response: error?.responseBody ?? error?.message
      })
      setStatusMessage(`Failed to send proposal (${error?.status ?? 'unknown'}): ${error?.message ?? 'Please check backend API.'}`)
    }
  }

  return (
    <section className="panel page-stack">
      <div>
        <h2>{order.title}</h2>
        <p>{order.description}</p>
        <p><strong>Category:</strong> {order.category}</p>
        <p><strong>Budget:</strong> ${order.budget}</p>
        <p><strong>Deadline:</strong> {order.deadline}</p>
        <p><strong>Minimum rating:</strong> {order.minRating}</p>
        <p><strong>Status:</strong> {order.status}</p>
      </div>

      {canApply && (
        <form className="form" onSubmit={submitProposal}>
          <h3>Apply to this order</h3>
          <label>Your price<input type="number" min="1" required value={price} onChange={e => setPrice(e.target.value)} /></label>
          <label>Message<textarea required value={message} onChange={e => setMessage(e.target.value)} /></label>
          <button className="primary-btn" type="submit">Send proposal</button>
          {statusMessage && <p>{statusMessage}</p>}
        </form>
      )}

      {isOrderOwner && (
        <section>
          <h3>Proposals</h3>
          {!proposals.length && <p>No proposals yet.</p>}
          {proposals.map(proposal => (
            <article key={proposal.id} className="freelancer-card">
              <p><strong>Freelancer #{proposal.freelancerId}</strong></p>
              <p>Price: ${proposal.price}</p>
              <p>Status: {proposal.status}</p>
              <p>{proposal.message}</p>
              <p>Skills: {(proposal.abilities || []).join(', ') || '—'}</p>
            </article>
          ))}
        </section>
      )}
    </section>
  )
}

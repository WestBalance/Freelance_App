import { useEffect, useState } from 'react'
import SkillSelector from '../components/SkillSelector'
import { getClientProfile, getFreelancerProfile, saveFreelancerProfile } from '../services/profileService'
import { completeOrder } from '../services/orderService'
import { createCheckoutSession } from '../services/paymentService'
import { acceptProposal } from '../services/proposalService'
import { getSkills } from '../services/skillService'

export default function ProfilePage({ user }) {
  const [skillOptions, setSkillOptions] = useState([])
  const [message, setMessage] = useState('')
  const [clientProfile, setClientProfile] = useState(null)
  const [clientProfileLoading, setClientProfileLoading] = useState(false)
  const [clientProfileError, setClientProfileError] = useState('')
  const [paymentLoading, setPaymentLoading] = useState(false)
  const [paymentError, setPaymentError] = useState('')
  const [form, setForm] = useState({
    userId: user?.id,
    about: '',
    skills: [],
    portfolioLinks: [''],
    reviews: [''],
    rating: 0
  })

  const loadClientProfile = async () => {
    setClientProfileLoading(true)
    setClientProfileError('')
    try {
      const data = await getClientProfile(user.id)
      setClientProfile(data)
    } catch (error) {
      setClientProfileError(error?.message ?? 'Could not load client profile')
    } finally {
      setClientProfileLoading(false)
    }
  }

  useEffect(() => {
    if (!user) return

    if (user.role === 'FREELANCER') {
      getSkills()
        .then(skills => {
          setSkillOptions(skills)
          return getFreelancerProfile(user.id)
            .then(profile => {
              const selectedByName = (profile.skills ?? []).map(name => skills.find(skill => skill.name === name)).filter(Boolean)
              setForm({
                userId: user.id,
                about: profile.about ?? '',
                skills: selectedByName,
                portfolioLinks: profile.portfolioLinks?.length ? profile.portfolioLinks : [''],
                reviews: profile.reviews?.length ? profile.reviews : [''],
                rating: profile.rating ?? 0
              })
            })
            .catch(() => {
              setForm(prev => ({ ...prev, userId: user.id }))
            })
        })
        .catch(() => setSkillOptions([]))
    }

    if (user.role === 'CLIENT') {
      loadClientProfile()
    }
  }, [user?.id, user?.role])

  if (!user) return <p className="panel">Please log in to view profile.</p>

  const updatePortfolio = (index, value) => {
    const next = [...form.portfolioLinks]
    next[index] = value
    setForm(prev => ({ ...prev, portfolioLinks: next }))
  }

  const addPortfolioLink = () => setForm(prev => ({ ...prev, portfolioLinks: [...prev.portfolioLinks, ''] }))


  const updateReview = (index, value) => {
    const next = [...form.reviews]
    next[index] = value
    setForm(prev => ({ ...prev, reviews: next }))
  }

  const addReview = () => setForm(prev => ({ ...prev, reviews: [...prev.reviews, ''] }))

  const submit = async e => {
    e.preventDefault()
    try {
      await saveFreelancerProfile({
        userId: user.id,
        about: form.about,
        rating: Number(form.rating),
        skillIds: form.skills.map(skill => skill.id),
        portfolioLinks: form.portfolioLinks.filter(Boolean),
        reviews: form.reviews.filter(Boolean)
      })
      setMessage('Profile saved.')
    } catch {
      setMessage('Could not save profile.')
    }
  }

  const handleAccept = async (orderId, proposalId) => {
    await acceptProposal(orderId, proposalId)
    await loadClientProfile()
  }

  const handleComplete = async orderId => {
    await completeOrder(orderId)
    await loadClientProfile()
  }

  const handlePay = async order => {
    if (!order?.id) return
    setPaymentLoading(true)
    setPaymentError('')
    try {
      const successUrl = `${window.location.origin}/profile?payment=success&orderId=${order.id}`
      const cancelUrl = `${window.location.origin}/profile?payment=cancelled&orderId=${order.id}`
      const checkout = await createCheckoutSession(order.id, successUrl, cancelUrl)
      if (!checkout?.url) throw new Error('Stripe checkout url is missing')
      window.location.href = checkout.url
    } catch (error) {
      setPaymentError(error?.message ?? 'Payment failed')
    } finally {
      setPaymentLoading(false)
    }
  }

  if (user.role === 'CLIENT') {
    if (clientProfileLoading) return <p className="panel">Loading client profile...</p>
    if (clientProfileError) return <p className="panel error-text">{clientProfileError}</p>
    if (!clientProfile) return <p className="panel">Client profile is empty.</p>

    const renderOrderBlock = (title, orders, options = {}) => (
      <section className="panel">
        <h3>{title}</h3>
        {!orders.length && <p>No orders.</p>}
        {orders.map(item => (
          <article key={item.order.id} className="client-order-block">
            <h4>{item.order.title} ({item.order.status})</h4>
            <p>{item.order.description}</p>
            {options.allowComplete && <button className="primary-btn" onClick={() => handleComplete(item.order.id)}>Mark Completed</button>}
            {options.allowPay && (
              <button
                className="secondary-btn"
                onClick={() => handlePay(item.order)}
                disabled={paymentLoading}
              >
                {paymentLoading ? 'Redirecting to Stripe...' : 'Pay with Stripe Checkout'}
              </button>
            )}
            <div className="proposal-list">
              <strong>Proposals:</strong>
              {!item.proposals.length && <p>No proposals yet.</p>}
              {item.proposals.map(proposal => (
                <div key={proposal.id} className="proposal-item">
                  <span>Freelancer #{proposal.freelancerId} — ${proposal.price} — {proposal.status}</span>
                  <span> Skills: {(proposal.abilities || []).join(', ') || '—'}</span>
                  {options.allowAccept && proposal.status === 'PENDING' && (
                    <button className="secondary-btn" onClick={() => handleAccept(item.order.id, proposal.id)}>Accept</button>
                  )}
                </div>
              ))}
            </div>
          </article>
        ))}
      </section>
    )

    return (
      <div className="page-stack">
        <h2>Client Profile</h2>
        {paymentError && <p className="panel error-text">{paymentError}</p>}
        {renderOrderBlock('Open orders', clientProfile.openOrders, { allowAccept: true })}
        {renderOrderBlock('Orders in progress', clientProfile.inProgressOrders, { allowPay: true, allowComplete: true })}
        {renderOrderBlock('Completed orders', clientProfile.completedOrders, {})}
      </div>
    )
  }

  return (
    <form className="panel form" onSubmit={submit}>
      <h2>Freelancer Profile</h2>
      <label>About<textarea value={form.about} onChange={e => setForm(prev => ({ ...prev, about: e.target.value }))} /></label>
      <small>Short professional summary.</small>

      <label>Skills</label>
      <SkillSelector options={skillOptions} selected={form.skills} onChange={skills => setForm(prev => ({ ...prev, skills }))} />
      <small>Search and select multiple skills.</small>

      <label>Portfolio Links</label>
      {form.portfolioLinks.map((link, idx) => (
        <input key={idx} value={link} onChange={e => updatePortfolio(idx, e.target.value)} placeholder="https://" />
      ))}
      <button type="button" className="secondary-btn" onClick={addPortfolioLink}>Add Link</button>
      <small>Add all relevant portfolio URLs.</small>

      <label>Reviews</label>
      {form.reviews.map((review, idx) => (
        <input key={`r-${idx}`} value={review} onChange={e => updateReview(idx, e.target.value)} placeholder="Client feedback" />
      ))}
      <button type="button" className="secondary-btn" onClick={addReview}>Add Review</button>

      <label>Rating<input type="number" value={form.rating} readOnly /></label>
      <small>Readonly rating from platform.</small>

      <button className="primary-btn" type="submit">Save Profile</button>
      {message && <p>{message}</p>}
    </form>
  )
}

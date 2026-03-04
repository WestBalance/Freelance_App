import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { createOrder, getCloneDraft } from '../services/orderService'

const EMPTY_ORDER = {
  title: '',
  description: '',
  category: 'CODING',
  budget: 100,
  deadline: '',
  minRating: 0,
  urgent: false,
  featured: false,
  paymentType: 'FIXED'
}

export default function CreateOrderPage({ user }) {
  const [form, setForm] = useState(EMPTY_ORDER)
  const [message, setMessage] = useState('')
  const [searchParams] = useSearchParams()

  useEffect(() => {
    const sourceId = searchParams.get('cloneFrom')
    if (!sourceId) return

    getCloneDraft(sourceId)
      .then(draft => {
        setForm({
          title: draft.title || '',
          description: draft.description || '',
          category: draft.category || 'CODING',
          budget: draft.budget || 100,
          deadline: draft.deadline || '',
          minRating: draft.minRating || 0,
          urgent: !!draft.urgent,
          featured: !!draft.featured,
          paymentType: draft.pricingMode || 'FIXED'
        })
      })
      .catch(() => setMessage('Failed to load clone draft.'))
  }, [searchParams])

  const update = (key, value) => setForm(prev => ({ ...prev, [key]: value }))

  const submit = async e => {
    e.preventDefault()
    try {
      const payload = {
        ...form,
        budget: Number(form.budget),
        minRating: Number(form.minRating),
        pricingMode: form.paymentType,
        estimatedHours: form.paymentType === 'HOURLY' ? 10 : 1
      }
      const created = await createOrder(payload)
      setMessage(`Order #${created.id} created successfully.`)
      setForm(EMPTY_ORDER)
    } catch (error) {
      console.error('Create order failed', {
        status: error?.status,
        response: error?.responseBody ?? error?.message
      })
      setMessage(`Failed to create order (${error?.status ?? 'unknown'}): ${error?.message ?? 'Please check backend API.'}`)
    }
  }

  if (!user || user.role !== 'CLIENT') {
    return <p className="panel">Only clients can create orders.</p>
  }

  return (
    <form className="panel form" onSubmit={submit}>
      <h2>Create Order</h2>
      <label>Title<input value={form.title} onChange={e => update('title', e.target.value)} required /></label>
      <label>Description<textarea value={form.description} onChange={e => update('description', e.target.value)} required /></label>
      <label>Category
        <select value={form.category} onChange={e => update('category', e.target.value)}>
          <option value="CODING">Coding</option>
          <option value="DESIGN">Design</option>
          <option value="MARKETING">Marketing</option>
          <option value="WRITING">Writing</option>
          <option value="VIDEO_PRODUCTION">Video Production</option>
        </select>
      </label>
      <label>Budget<input type="number" min="1" value={form.budget} onChange={e => update('budget', e.target.value)} required /></label>
      <label>Deadline<input type="date" value={form.deadline} onChange={e => update('deadline', e.target.value)} /></label>
      <label>Minimum Rating
        <select value={form.minRating} onChange={e => update('minRating', e.target.value)}>
          {[0, 1, 2, 3, 4, 4.5].map(rate => <option key={rate} value={rate}>{rate}+</option>)}
        </select>
      </label>
      <label className="inline"><input type="checkbox" checked={form.urgent} onChange={e => update('urgent', e.target.checked)} />Urgent</label>
      <label className="inline"><input type="checkbox" checked={form.featured} onChange={e => update('featured', e.target.checked)} />Featured</label>

      <fieldset>
        <legend>Payment Type</legend>
        <label className="inline"><input type="radio" name="paymentType" checked={form.paymentType === 'FIXED'} onChange={() => update('paymentType', 'FIXED')} />Fixed</label>
        <label className="inline"><input type="radio" name="paymentType" checked={form.paymentType === 'HOURLY'} onChange={() => update('paymentType', 'HOURLY')} />Hourly</label>
      </fieldset>

      <button className="primary-btn" type="submit">Submit</button>
      {message && <p>{message}</p>}
    </form>
  )
}

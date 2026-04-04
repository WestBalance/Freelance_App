import { useMemo, useState } from 'react'

const DEMO_CARD = '4242 4242 4242 4242'

export default function StripeDemoCheckoutModal({
  isOpen,
  amount,
  userEmail,
  isProcessing,
  errorMessage,
  onClose,
  onConfirm
}) {
  const [form, setForm] = useState({
    email: userEmail ?? '',
    fullName: '',
    country: 'United States',
    address: '',
    cardNumber: DEMO_CARD,
    expiry: '',
    cvc: '',
    billingSame: true
  })

  const formattedAmount = useMemo(() => {
    const numericAmount = Number(amount)
    if (Number.isNaN(numericAmount)) return '0.00'
    return numericAmount.toFixed(2)
  }, [amount])

  if (!isOpen) return null

  const update = (key, value) => setForm(prev => ({ ...prev, [key]: value }))

  const submit = async e => {
    e.preventDefault()
    await onConfirm()
  }

  return (
    <div className="modal-overlay" onClick={isProcessing ? undefined : onClose}>
      <div className="modal stripe-demo-modal" onClick={e => e.stopPropagation()}>
        <form className="stripe-demo-form" onSubmit={submit}>
          <label>
            Email
            <input
              type="email"
              required
              value={form.email}
              onChange={e => update('email', e.target.value)}
              placeholder="you@example.com"
            />
          </label>

          <h4>Shipping address</h4>
          <label>
            Name
            <input required value={form.fullName} onChange={e => update('fullName', e.target.value)} placeholder="Full name" />
          </label>

          <label>
            Country
            <select value={form.country} onChange={e => update('country', e.target.value)}>
              <option>United States</option>
              <option>Canada</option>
              <option>United Kingdom</option>
            </select>
          </label>

          <label>
            Address
            <input required value={form.address} onChange={e => update('address', e.target.value)} placeholder="Street and house" />
          </label>

          <h4>Payment details</h4>
          <label>
            Card information
            <input
              required
              value={form.cardNumber}
              onChange={e => update('cardNumber', e.target.value)}
              placeholder="4242 4242 4242 4242"
              inputMode="numeric"
            />
          </label>

          <div className="stripe-inline-fields">
            <label>
              MM / YY
              <input required value={form.expiry} onChange={e => update('expiry', e.target.value)} placeholder="12 / 34" />
            </label>
            <label>
              CVC
              <input required value={form.cvc} onChange={e => update('cvc', e.target.value)} placeholder="123" inputMode="numeric" />
            </label>
          </div>

          <label className="inline stripe-checkbox">
            <input
              type="checkbox"
              checked={form.billingSame}
              onChange={e => update('billingSame', e.target.checked)}
            />
            Billing address is same as shipping
          </label>

          <p className="stripe-demo-hint">Demo mode: use test card {DEMO_CARD}. No real charge.</p>

          <button className="primary-btn stripe-pay-btn" type="submit" disabled={isProcessing}>
            {isProcessing ? 'Processing...' : `Pay $${formattedAmount}`}
          </button>
          {errorMessage && <p className="error-text">{errorMessage}</p>}
        </form>
        <button className="close-link" onClick={onClose} disabled={isProcessing}>Cancel</button>
      </div>
    </div>
  )
}

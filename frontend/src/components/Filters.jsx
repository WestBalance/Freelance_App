import { useState } from 'react'

const DEFAULT = {
  category: 'ALL',
  budgetMin: '',
  budgetMax: '',
  minRating: ''
}

export default function Filters({ onApply }) {
  const [filters, setFilters] = useState(DEFAULT)

  const update = (key, value) => setFilters(prev => ({ ...prev, [key]: value }))

  return (
    <section className="panel filters">
      <h2>Filters</h2>
      <div className="filters-grid">
        <label>
          Select Category
          <select value={filters.category} onChange={e => update('category', e.target.value)}>
            <option value="ALL">All</option>
            <option value="CODING">Coding</option>
            <option value="DESIGN">Design</option>
            <option value="MARKETING">Marketing</option>
            <option value="WRITING">Writing</option>
            <option value="VIDEO_PRODUCTION">Video Production</option>
          </select>
        </label>
        <label>
          Budget min
          <input type="number" min="0" value={filters.budgetMin} onChange={e => update('budgetMin', e.target.value)} />
        </label>
        <label>
          Budget max
          <input type="number" min="0" value={filters.budgetMax} onChange={e => update('budgetMax', e.target.value)} />
        </label>
        <label>
          Minimum Rating
          <select value={filters.minRating} onChange={e => update('minRating', e.target.value)}>
            <option value="">Any</option>
            {[1, 2, 3, 4, 4.5].map(v => <option key={v} value={v}>{v}+</option>)}
          </select>
        </label>
      </div>
      <button className="primary-btn" onClick={() => onApply(filters)}>Apply Filters</button>
    </section>
  )
}

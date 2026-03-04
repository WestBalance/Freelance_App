import { useMemo, useState } from 'react'
import Filters from '../components/Filters'
import OrderCard from '../components/OrderCard'
import { useOrders } from '../services/orderService'

export default function HomePage({ user }) {
  const { orders, loading, error } = useOrders()
  const [activeFilters, setActiveFilters] = useState(null)

  const filteredOrders = useMemo(() => {
    if (!activeFilters) return orders

    return orders.filter(order => {
      const categoryMatch = activeFilters.category === 'ALL' || order.category === activeFilters.category
      const minMatch = !activeFilters.budgetMin || order.budget >= Number(activeFilters.budgetMin)
      const maxMatch = !activeFilters.budgetMax || order.budget <= Number(activeFilters.budgetMax)
      const ratingMatch = !activeFilters.minRating || order.minRating >= Number(activeFilters.minRating)
      return categoryMatch && minMatch && maxMatch && ratingMatch
    })
  }, [orders, activeFilters])

  return (
    <section className="page-stack">
      <Filters onApply={setActiveFilters} />
      {loading && <p className="panel">Loading orders...</p>}
      {error && <p className="panel error-text">{error}</p>}
      {!loading && !error && (
        <div className="orders-list">
          {filteredOrders.map(order => <OrderCard key={order.id} order={order} user={user} />)}
          {!filteredOrders.length && <p className="panel">No orders found by selected filters.</p>}
        </div>
      )}
    </section>
  )
}

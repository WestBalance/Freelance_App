export default function Footer() {
  return (
    <footer className="footer">
      <div className="footer-left">
        <label htmlFor="currency">Currency:</label>
        <select id="currency" defaultValue="USD">
          <option value="USD">USD</option>
          <option value="EUR">EUR</option>
          <option value="GBP">GBP</option>
        </select>
      </div>
      <p>© {new Date().getFullYear()} TaskLink</p>
    </footer>
  )
}

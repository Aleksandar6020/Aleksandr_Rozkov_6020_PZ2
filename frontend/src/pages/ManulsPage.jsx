import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { apiFetch } from '../services/api.js'

const LIMIT = 6

function ManulsPage() {
  const [manuls, setManuls] = useState([])
  const [sortBy, setSortBy] = useState('createdAt')
  const [page, setPage] = useState(1)
  const [totalPages, setTotalPages] = useState(1)
  const [total, setTotal] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const loadManuls = async () => {
      try {
        setLoading(true)
        setError(null)
        const order = sortBy === 'name' ? 'asc' : 'desc'
        const data = await apiFetch(`/manuls?sortBy=${sortBy}&order=${order}&page=${page}&limit=${LIMIT}`)
        setManuls(data.items || data)
        setTotalPages(data.pages || 1)
        setTotal(data.total || (data.items || data).length)
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }
    loadManuls()
  }, [sortBy, page])

  const handleSortChange = (event) => {
    setSortBy(event.target.value)
    setPage(1)
  }

  if (loading) return <div className="container"><p>Loading…</p></div>
  if (error) return <div className="container"><p>Error: {error}</p></div>

  return (
    <div className="container">
      <h1>Manuls</h1>
      <div className="formRow">
        <label className="label">Sort by</label>
        <select className="input" value={sortBy} onChange={handleSortChange}>
          <option value="createdAt">Newest</option>
          <option value="likesCount">Likes</option>
          <option value="name">Name</option>
        </select>
      </div>
      <div className="cardGrid">
        {manuls.map((m) => (
          <div key={m.id} className="card">
            <img className="cardImage" src={m.photoUrl} alt={m.name} />
            <h3>{m.name}</h3>
            <p>{m.shortDescription}</p>
            <Link className="button" to={`/manuls/${m.id}`}>Open</Link>
          </div>
        ))}
      </div>

      <div className="pagination">
        <button className="button buttonSecondary" type="button" disabled={page <= 1} onClick={() => setPage((p) => p - 1)}>
          Previous
        </button>
        <span>Page {page} of {totalPages} ({total} manuls)</span>
        <button className="button buttonSecondary" type="button" disabled={page >= totalPages} onClick={() => setPage((p) => p + 1)}>
          Next
        </button>
      </div>
    </div>
  )
}

export default ManulsPage

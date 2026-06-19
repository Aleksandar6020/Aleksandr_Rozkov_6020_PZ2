import { useContext, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { AuthContext } from '../context/AuthContext.jsx'
import { apiFetch } from '../services/api.js'

function ManulDetailsPage() {
  const { id } = useParams()
  const { currentUser, role } = useContext(AuthContext)
  const [manul, setManul] = useState(null)
  const [comments, setComments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [liked, setLiked] = useState(false)
  const [suggestionText, setSuggestionText] = useState('')
  const [commentText, setCommentText] = useState('')

  useEffect(() => {
    const loadPage = async () => {
      try {
        setLoading(true)
        setError(null)
        const [manulData, commentsData] = await Promise.all([
          apiFetch(`/manuls/${id}`),
          apiFetch(`/manuls/${id}/comments`),
        ])
        setManul(manulData)
        setComments(commentsData)
        setLiked(Boolean(manulData.likedByCurrentUser))
      } catch (e) {
        setError(e?.message || 'Failed to load manul')
        setManul(null)
      } finally {
        setLoading(false)
      }
    }
    loadPage()
  }, [id])

  const handleLike = async () => {
    if (!currentUser) return alert('Login required')
    if (!manul || liked) return

    try {
      const data = await apiFetch(`/manuls/${manul.id}/like`, { method: 'POST' })
      setManul((prev) => ({ ...prev, likesCount: data.likesCount }))
      setLiked(true)
    } catch (e) {
      alert(e.message || 'Like failed')
    }
  }

  const handleCommentSubmit = async (event) => {
    event.preventDefault()
    if (!currentUser) return alert('Login required')

    try {
      const saved = await apiFetch(`/manuls/${id}/comments`, {
        method: 'POST',
        body: JSON.stringify({ content: commentText }),
      })
      setComments((prev) => [saved, ...prev])
      setCommentText('')
    } catch (e) {
      alert(e.message || 'Comment failed')
    }
  }

  const handleDeleteComment = async (commentId) => {
    try {
      await apiFetch(`/comments/${commentId}`, { method: 'DELETE' })
      setComments((prev) => prev.filter((comment) => comment.id !== commentId))
    } catch (e) {
      alert(e.message || 'Delete failed')
    }
  }

  const handleSuggestionSubmit = async (event) => {
    event.preventDefault()
    if (!currentUser) return alert('Login required')

    try {
      await apiFetch('/suggestions', {
        method: 'POST',
        body: JSON.stringify({ manulId: id, type: 'STORY', content: suggestionText, status: 'PENDING' }),
      })
      setSuggestionText('')
      alert('Suggestion sent')
    } catch (e) {
      alert(e.message || 'Suggestion failed')
    }
  }

  const canDeleteComment = (comment) => {
    if (!currentUser) return false
    return role === 'admin' || comment.userId === currentUser.id
  }

  return (
    <div className="container">
      {loading && <p>Loading…</p>}
      {error && <p>Error: {error}</p>}
      {!loading && !error && manul && (
        <>
          <h1>{manul.name}</h1>
          <div className="details">
            <img className="detailsImage" src={manul.photoUrl} alt={manul.name} />
            <div className="detailsBody">
              <p>{manul.longStory}</p>
              <div className="infoBox">
                <p><b>Location type:</b> {manul.locationType || '-'}</p>
                <p><b>Region:</b> {manul.region || '-'}</p>
                <p><b>Created:</b> {manul.createdAt || '-'}</p>
              </div>
              <div className="likeRow">
                <span className="likeBadge" title="Likes">{manul.likesCount}</span>
                {currentUser ? (
                  <button className={`button likeButton ${liked ? 'likeButtonActive' : ''}`} type="button" onClick={handleLike} disabled={liked}>{liked ? '♥' : '♡'}</button>
                ) : (
                  <span className="muted">Login to like</span>
                )}
              </div>
            </div>
          </div>

          <section className="sectionBlock">
            <h2>Comments</h2>
            {comments.length === 0 && <p className="muted">No comments yet.</p>}
            <div className="commentsList">
              {comments.map((comment) => (
                <div className="comment" key={comment.id}>
                  <p>{comment.content}</p>
                  <small>{comment.authorEmail} · {comment.createdAt}</small>
                  {canDeleteComment(comment) && (
                    <button className="button buttonDanger buttonSmall" type="button" onClick={() => handleDeleteComment(comment.id)}>Delete</button>
                  )}
                </div>
              ))}
            </div>
            {currentUser ? (
              <form className="form" onSubmit={handleCommentSubmit}>
                <div className="formRow">
                  <label className="label">Add comment</label>
                  <textarea className="input" value={commentText} onChange={(e) => setCommentText(e.target.value)} required />
                </div>
                <button className="button" type="submit">Submit comment</button>
              </form>
            ) : (
              <p className="muted">Login to write a comment.</p>
            )}
          </section>

          <section className="sectionBlock">
            <h2>Suggest a story</h2>
            {currentUser ? (
              <form className="form" onSubmit={handleSuggestionSubmit}>
                <div className="formRow">
                  <label className="label">Your story</label>
                  <textarea className="input" value={suggestionText} onChange={(e) => setSuggestionText(e.target.value)} required />
                </div>
                <button className="button" type="submit">Submit suggestion</button>
              </form>
            ) : (
              <p className="muted">Login to suggest a story.</p>
            )}
          </section>
        </>
      )}
    </div>
  )
}

export default ManulDetailsPage

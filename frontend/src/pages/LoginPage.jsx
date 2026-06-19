import { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AuthContext } from '../context/AuthContext.jsx'
import { apiFetch } from '../services/api.js'

function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()
  const { login } = useContext(AuthContext)

  const handleSubmit = async (event) => {
    event.preventDefault()

    try {
      const data = await apiFetch('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      })
      login(data.user, data.token)
      navigate('/manuls')
    } catch (e) {
      alert(e.message || 'Invalid email or password')
    }
  }

  return (
    <div className="container">
      <h1>Login</h1>
      <form className="form" onSubmit={handleSubmit}>
        <div className="formRow">
          <label className="label" htmlFor="login-email">Email</label>
          <input id="login-email" name="email" className="input" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required autoComplete="email" />
        </div>
        <div className="formRow">
          <label className="label" htmlFor="login-password">Password</label>
          <input id="login-password" name="password" className="input" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required autoComplete="current-password" />
        </div>
        <button className="button" type="submit">Login</button>
      </form>
    </div>
  )
}

export default LoginPage

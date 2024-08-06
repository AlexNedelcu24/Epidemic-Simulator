import React, { useState, useEffect } from 'react'

import { Helmet } from 'react-helmet'
import { useHistory } from 'react-router-dom'
import { toast } from 'react-toastify';
import axios from '../api/axios';

import './login.css'

const LOGIN_URL = '/login';



const Login = (props) => {

  const history = useHistory()

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  useEffect(() => {
    // Detectează revenirea pe pagina de login și curăță istoricul
    if (history.action === 'POP') {
      history.replace('/');
    }
  }, [history]);

  const handleLogin = async () => {
    // logica de verificare a autentificarii

    try {
      const response = await axios.post(LOGIN_URL,
          JSON.stringify({ username, password }),
          {
              headers: { 'Content-Type': 'application/json'}
          }
      );
      // TODO: remove console.logs before deployment
      

      setUsername('');
      setPassword('');

      const userId = JSON.stringify(response?.data)

      console.log(userId);

      localStorage.setItem('userId', userId);

      toast.done("Success");
      history.push('/main-page')
  } catch (err) {
      if (!err?.response) {
          toast.error('No Server Response');
      } else if (err.response?.status === 400) {
        toast.error('Incorrect username or password');
      } else {
          toast.error('Login Failed')
      }
      
  }
  }

  const handleRegister = () => {
    history.push('/register')
  }

  return (
    <div className="login-container">
      <Helmet>
        <title>Epidemic Simulator</title>
        <meta property="og:title" content="Epidemic Simulator" />
      </Helmet>
      <div className="login-home">
        <div className="login-log-in">
          <h1 className="login-text log">Login</h1>
          <label id="error_login" className="login-text1 error">
            Incorrect username or password
          </label>
          <div className="login-container1">
            <form className="login-form">
              <input
                type="text"
                placeholder="Username"
                className="login-textinput input"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <input
                type="password"
                placeholder="Password"
                className="login-textinput1 input"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </form>
            <div className="login-container2">
              <input type="checkbox" checked={false} />
              <span className="login-text2 st">Remember me</span>
            </div>
            <button type="button" className="login-button button" onClick={handleLogin}>
              Login
            </button>
          </div>
          <div className="login-container3">
            <span className="login-text3 acc">Don&apos;t have an account?</span>
            <button type="button" className="login-button1 button reg" onClick={handleRegister}>
              Register
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Login

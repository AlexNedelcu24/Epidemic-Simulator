import React, { useState } from 'react'

import { Helmet } from 'react-helmet'
import { useHistory } from 'react-router-dom'
import './register.css'
import axios from '../api/axios';

import { toast } from 'react-toastify';

const REGISTER_URL = '/register';

const Register = (props) => {

  const history = useHistory()

  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleRegister = async () => {
    // logica de verificare a autentificarii

    if(password != confirmPassword){
      toast.error("Password confirmation error");
      return;
    }

    try {
      const response = await axios.post(REGISTER_URL,
          JSON.stringify({ username, email, password }),
          {
              headers: { 'Content-Type': 'application/json'}
          }
      );
      
      console.log(JSON.stringify(response?.data));

      setUsername('');
      setPassword('');
      setConfirmPassword('');

      toast.done("Success");
      history.push('/')
  } catch (err) {
      if (!err?.response) {
          toast.error('No Server Response');
      } else if (err.response?.status === 400) {
          toast.error(err.response?.data);
      } else {
          toast.error('Registration Failed')
      }
      
  }

  }

  return (
    <div className="register-container">
      <Helmet>
        <title>Register - Epidemic Simulator</title>
        <meta property="og:title" content="Epidemic Simulator" />
      </Helmet>
      <div className="register-home">
        <div className="register-register">
          <h1 className="log">Register</h1>
          <label className="register-text1 error">Label</label>
          <form className="register-form">
            <input
              type="text"
              placeholder="Username"
              className="register-textinput input"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <input
              type="text"
              placeholder="Email"
              className="register-textinput1 input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <input
              type="text"
              placeholder="Create password"
              className="register-textinput2 input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <input
              type="text"
              placeholder="Confirm Password"
              className="register-textinput3 input"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            <button type="button" className="register-button button" onClick={handleRegister}>
              Register
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

export default Register

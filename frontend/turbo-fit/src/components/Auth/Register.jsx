import React, { useState } from 'react';
import axios from 'axios';
import './auth.css';
import { useNavigate } from 'react-router-dom';

const Register = () => {
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = {
      email,
      phone,
      username,
      password,
    };

    try {
      const response = await axios.post('http://192.168.1.15:8081/auth/register', data);
      alert('Успешно регистриран:', response.data);
      navigate("/login")
    } catch (error) {
      alert('Грешка при регистрацията:', error);
    }
  };

  return (
     
    
    <div className='reg-container'>
      <div className='header'>
        <div className='text'>Register</div>
      </div>
     <form onSubmit={handleSubmit}>
      <div className='inputs'>

        <div className='input'>
          <input
            name='username'
            type="text"
            placeholder='Username'
            required
            minLength={6}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>

        <div className='input'>
          <input
          name='password'
            type="password"
            placeholder='Password'
            required
            minLength={6}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <div className='input'>
          <input
          name='email'
            type="email"
            placeholder='Email'
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>

        <div className='input'>
          <input
          name='tel'
            type="tel"
            placeholder='Phone'
            required
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
          />
        </div>

        <div className="submit-container">
          <button className="submit-btn" type="submit">
            Sign Up
          </button>
        </div>

      </div>
    </form>
    </div>
  );
}

export default Register;

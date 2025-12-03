import React, {useRef,useState,useEffect} from 'react'
import axios from 'axios'
import "./auth.css"

import {Link, useNavigate } from 'react-router-dom';


const Login = ({setIsLoggedIn, setLoggedUser}) => {
  const navigate = useNavigate();


      const [username, setUsername] = useState('');
      const [password, setPassword] = useState('');
      
      const handleSubmit = async (e) => {
    e.preventDefault();


    const data = {
      username,
      password,
      
    };

    try {
      const response = await axios.post('http://localhost:8080/auth/login', data,);

      console.log('Login response:', response.data);
      localStorage.setItem('token',response.data.token);
      localStorage.setItem('userId',response.data.userId);
  const { username, userId } = response.data;
  const userData = {
      userId:  response.data.userId,
      username: response.data.username,
      car:      response.data.car,     
      points : response.data.points,
      token : response.data.token,
      totalWorkouts   : response.data.tota,
      role: response.data.role       
    };
setLoggedUser(userData);
console.log(response.data);
localStorage.setItem('user', JSON.stringify(userData));
setIsLoggedIn(true);

if(userData.car){
navigate('/');

}else navigate("/selectCar")

   
    } catch (error) {
      alert('Грешно потребителско име или парола:', error);
    }
  };
   return (
     
    
    <div className='reg-container'>
      <div className='header'>
        <div className='text'>Login</div>
      </div>
     <form onSubmit={handleSubmit}>
      <div className='inputs'>

        <div className='input'>
          <input
            name='username'
            type="text"
            placeholder='Username'
            required
            minLength={3}
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
            minLength={5}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <div className="register-now">
          
          Not Registered? &ensp;<Link to="/register"> Register Now</Link>
        </div>
        <div className="submit-container">
          
          <button className="submit-btn" type="submit">
            Login
          </button>
        </div>

      </div>
    </form>
    </div>
  );
}

export default Login
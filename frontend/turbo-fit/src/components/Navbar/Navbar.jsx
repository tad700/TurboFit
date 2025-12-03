
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './navbar.css';

function Navbar({ isLoggedIn, setIsLoggedIn }) {
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isAdmin, setIsAdmin]= useState(false);
 const storedUser = JSON.parse(localStorage.getItem('user'));

  const handleLogout = () => {
    localStorage.clear();
    setIsLoggedIn(false);
    navigate('/login');
  };

  const toggleMenu = () => {
    setIsMenuOpen(prev => !prev);
  };
  const handleClick = () =>{
    setIsMenuOpen(false)
  }

  return (
    <div className="navbar">
      <div className="navbar-left">
        <Link to="/"><h1>TurboFit</h1></Link>
      </div>

      <div className="menu-toggle" onClick={toggleMenu}>
        ☰
      </div>

      <ul className={`navbar-links ${isMenuOpen ? 'open' : ''}`}>
        {!isLoggedIn && (
          <>
            <li><Link to="/login" onClick={handleClick}>Login</Link></li>
            <li><Link to="/register" onClick={handleClick}>Register</Link></li>
          </>
        )}
        {storedUser?.role === "ADMIN" &&(
          <>
           <li>
            <Link to="/admin" onClick={handleClick}>
            Admin
            </Link>
            </li>
           </>
        )}


        {isLoggedIn && (
          <>
            <li><Link to="/"onClick={handleClick}>Home</Link></li>
         
            <li><Link to="/workout"onClick={handleClick}>Workout</Link></li>
            <li><Link to="/history"onClick={handleClick}>History</Link></li>
            <li><Link to="/login" onClick={() =>{handleLogout(); handleClick();}}>Logout</Link></li>
          </>
        )}
      </ul>
    </div>
  );
}

export default Navbar;

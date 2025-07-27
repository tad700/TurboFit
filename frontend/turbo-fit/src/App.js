import logo from './logo.svg';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';

import Register from './components/Auth/Register';
import Navbar from './components/Navbar/Navbar';
import Login from './components/Auth/Login';
import SelectCar from './components/SelectCar';
import Home from './components/Home';
import React, {useState,useEffect} from 'react';
import Workout from './components/Workout';
import History from './components/History';
import Admin from './components/Admin';



function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loggedUser, setLoggedUser] = useState({});

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      setLoggedUser(parsedUser);
      setIsLoggedIn(true);
    }
  }, []);

  return (
    <Router>
      <div className="App">
        <Navbar isLoggedIn={isLoggedIn} setIsLoggedIn={setIsLoggedIn} />
        <Routes>
        <Route path="/login" element={<Login setLoggedUser={setLoggedUser} setIsLoggedIn={setIsLoggedIn}   />} />
        <Route path="/register" element={<Register />} />
         <Route path="/selectCar" element={<SelectCar userId={loggedUser.userId} />}/>
         <Route path="/workout" element={<Workout />} />
           <Route path="/history" element={<History />} />
            <Route path="/admin" element={<Admin />} />
        <Route path="/" element={<Home user={loggedUser} />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;

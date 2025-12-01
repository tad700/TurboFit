import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Home.css';
import { useNavigate } from 'react-router-dom';

export default function Home({ user }) {
  const [car, setCar] = useState(null);
  const [points, setPoints] = useState(0);
  const [totalWorkouts, setTotalWorkouts] = useState(0);
  const navigate = useNavigate();
  const { username, userId, password } = user || {};

  useEffect(() => {
  const storedUser = JSON.parse(localStorage.getItem('user'));
    if (!storedUser || !storedUser.username || !storedUser.password) {
      navigate('/login');
      return;
    }
    
  
  axios.get(`http://localhost:8081/api/users/${storedUser.userId}/car`, {
  auth: { username: storedUser.username, password: storedUser.password }
})
.then(res => {
  console.log('Car data:', res.data);
  setCar(res.data);
})
.catch(err => {
  console.error('Error loading car:', err.response || err);
  if (err.response?.status === 404) {
    setCar(null);
  }
});

  axios.get(`http://localhost:8081/api/users/${storedUser.userId}`, {
    auth: { username:storedUser.username, password:storedUser.password }
  })
  .then(res => {
    const updatedUser = res.data;
    setPoints(updatedUser.points);
    setTotalWorkouts(updatedUser.totalWorkouts);
    console.log(totalWorkouts)
  })
  .catch(err => {
    console.error('Грешка при зареждане на потребител:', err);
  });

}, [userId, username, password]);

  const handleAddHP = () => {
  const storedUser = JSON.parse(localStorage.getItem('user'));
  axios.put(`http://localhost:8081/api/cars/update-horsepower/${storedUser.userId}`, {
    auth: {
      username: storedUser.username,
      password: storedUser.password
    }
  })
  .then(res => {
    setCar(res.data);
    setPoints(prev => prev - 10);
  })
  .catch(err => {
    alert('Грешка при добавяне на конски сили: ' + err.message);
  });
}
const calculateWorkoutRank = (totalWorkouts) => {
   if (totalWorkouts > 45) {
    return "Track Master";     
  }
  else if (totalWorkouts > 30) {
    return "Track Master";     
  } else if (totalWorkouts > 20) {
    return "Speedster";      
  } else if (totalWorkouts > 10) {
    return "Challenger";      
  } else if(totalWorkouts>0){
    return "Rookie";    
  }
};



  return (
    <div className="user-dashboard">

      <div className="user-details">
        
      
        <h1>Welcome <br />{username ? ` ${username}` : ''}!</h1>
<p>
  Rank: {totalWorkouts ? calculateWorkoutRank(totalWorkouts) : "No workouts yet"}
</p>


        <div className="points">      <h2>Points: {points ?? 0}</h2></div>
        <h2>Finished Workouts {totalWorkouts ? `${totalWorkouts}` : ''}</h2>
      
      
    </div>
      

      {car ? (
        <div className="car-info">
          <h3>Selected Car</h3>
          <img
            src={`http://localhost:8081${car.baseCar.imageUrl}`}
            alt={car.carName}
            style={{ width: 200, borderRadius: 10 }}
          />
          <p><strong>Brand:</strong> {car.baseCar.carName}</p>
          <p><strong>HP:</strong> {car.customHorsePower} HP</p>
          <button onClick={handleAddHP}
          >Add HP : 10 points</button>
          
        </div>
        
      ) : (
        <p>You dont have car <br />Go To "Select Car".</p>
      )}
      

    
    </div>
    
    
    

  );
}

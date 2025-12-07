import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './SelectCar.css';
import { useNavigate } from 'react-router-dom';

export default function SelectCar({ userId }) {
  const [cars, setCars] = useState([]);
  const [user,setUser] = useState({username: '',password: ''});
  const [token,setToken]=useState("");
const navigate = useNavigate();
  useEffect(() => {
   
        const storedUser = JSON.parse(localStorage.getItem('user'));
        console.log(storedUser);
        const token = localStorage.getItem('token');
    if (!storedUser || !storedUser.username) {
       navigate("/login");   
       return;
    }
      setUser(storedUser);
    
    
    if(storedUser.car != null){
      navigate("/");
      return;
    }
    console.log("User " +storedUser.username)
    axios.get(`${process.env.REACT_APP_API_URL}/api/cars`, {
        headers: {
            'Authorization': `Bearer ${token}` 
        }
    })
      .then(res => {
        setCars(res.data);
    console.log(res.data);})

      .catch(err => console.error('Грешка при зареждане на коли:', err));
  }, []);
   const handleSelect = (carId) => {
    console.log("Изпращам PUT заявка за userId:", userId, "carId:", carId);
 
axios.put(
 
  `${process.env.REACT_APP_API_URL}/api/users/${userId}/selectCar/${carId}`,{
    
        headers: {
            'Authorization': `Bearer ${token}` 
        }
    })
      .then(() => {
        alert('Кола избрана успешно!');
        navigate("/")
      })
      .catch(err => {
        console.error('Грешка при избора:', err.response?.data || err.message);
      });
  };

  return (
    <div className="select-car">
      <h2>Избери своята кола:</h2>
      <div className="car-cards">
        {cars.map(car => (
          <div
            className="car-card"
            key={car.carId}
            onClick={() => handleSelect(car.carId)}
          >
            <img src={`/cars/${car.imageUrl}`} alt={car.carName} />
            <h4>{car.carName}</h4>
            <p>{car.horsePower} HP</p>
          </div>
        ))}
      </div>
    </div>
  );
}

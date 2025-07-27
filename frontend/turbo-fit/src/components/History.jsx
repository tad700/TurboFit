import React from 'react'
import { useEffect,useState } from 'react'
import axios from 'axios'
import { Navigate, useNavigate } from 'react-router-dom';
import './history.css'

export default function History() {
const [user,setUser] = useState('');
const[workouts,setWorkouts] = useState([])
const navigate = useNavigate();
 const [expandedWorkoutId, setExpandedWorkoutId] = useState(null);

useEffect(() =>{
  
 const storedUser = JSON.parse(localStorage.getItem('user'));
    if (!storedUser || !storedUser.username) {
       navigate("/login");   
       return;
    }
      setUser(storedUser);


console.log(storedUser);
axios.get(`http://192.168.1.15:8081/api/users/userWorkouts/${storedUser.userId}`,{
  auth:{ 
    username:user.username,
    password:user.password
  }

}

).then(res => setWorkouts(res.data))
      .catch(err => console.error('Грешка при зареждане на тренировки:', err));
  } , []);


  const toggleExpand = (id) => {
    setExpandedWorkoutId((prev) => (prev === id ? null : id));
  };



return (
  <div>
    <div className="history-container">
      <h1>История на тренировките</h1>

      {(!workouts || workouts.length === 0) ? (
        <p>Нямате тренировки.</p>
      ) : (
        workouts.map((workout) => (
          <div
            className="history-card"
            key={workout.workoutId}
            onClick={() => toggleExpand(workout.workoutId)}
            style={{ cursor: "pointer" }}
          >  <h2>{workout.name || "Без име"} </h2>
            <div className="history-card-header">
            
              <p>Старт: {new Date(workout.startTime).toLocaleString([],{hour: '2-digit', minute: '2-digit'})} {new Date(workout.startTime).getDate()}{'/'}{new Date(workout.startTime).getMonth() + 1}{'/'}{new Date(workout.startTime).getFullYear()}
             
              </p>
         
              <p>
                  Край:{" "}
                  {workout.endTime
                    ? new Date(workout.endTime).toLocaleString([],{hour: '2-digit', minute: '2-digit'})
                    : "Все още активна"} {new Date(workout.startTime).getDate()}{'/'}{new Date(workout.startTime).getMonth() + 1}{'/'}{new Date(workout.startTime).getFullYear()}
                </p>
           
            </div>
           
            
            {expandedWorkoutId === workout.workoutId && (
              <div className="expanded-section">
               
                
                <p>Упражнения:</p>
                {workout.exercises.map((exercise, index) => (
                  <div className="exercise-container" key={index}>
                    <div className="exercise-details">
                      <h4>{exercise.name}</h4>
                      <p>Sets: {exercise.sets}</p>
                      <p>Reps: {exercise.reps}</p>
                      <p>Total Weight: {exercise.weight}</p>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))
      )}
    </div>
  </div>
);

}

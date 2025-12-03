import  {React, useState} from 'react'
import "./admin.css"
import axios from 'axios'


export default function Admin() {


const [username, setUsername] = useState('');
const [addCarName,setAddCarName] = useState('');
const [deleteCarName,setDeleteCarName] = useState('');
const [carHP,setCarHP] = useState('');
const[newExerciseName,setNewExerciseName] = useState('');
const[deleteExerciseName,setDeleteExerciseName] = useState('');
const[muscleGroup,setMuscleGroup] = useState('');


 const storedUser = JSON.parse(localStorage.getItem('user'));
const handleDeleteUser = () => {
   
  axios
    .delete(`http://localhost:8080/admin/delete/user/${username}`,
        {
            auth:{
                username: storedUser.username,
                password: storedUser.password
            }
        }
    )
    .then(res => {
       
        console.log(res)
         alert("Deleted user :"+username)
    })
    .catch(err =>
      console.error('Грешка при изтриване на потребител:', err)
    );

};

const handleAddCar = () => {
  axios
    .post(
      'http://localhost:8080/admin/addCar/',
      {
        carName: addCarName,
        horsePower: carHP,
        imageUrl: `/images/${addCarName.toLocaleLowerCase()}.png`
      },
      {
        auth: {
          username: storedUser.username,
          password: storedUser.password
        }
      }
    )
    .then(res => {
        console.log("Added Car " + addCarName)
        console.log(res.data)
          alert("Added Car :"+addCarName)

})
    .catch(err =>
      console.error('Грешка при добавяне на кола:', err)
    );
    
};
const handleDeleteCar = () => {
  axios
    .delete(
      `http://localhost:8080/admin/delete/car/${deleteCarName}`,
      {
        auth: {
          username: storedUser.username,
          password: storedUser.password
        }
      }
    )
    .then(res => {
    console.log("Deleted Car " + addCarName)
      console.log(res.data);
      alert("Removed Car: "+addCarName)
      
    })
    .catch(err =>
      console.error('Грешка при изтриване на кола:', err)
    );
};


const handleAddExercise = () => {

  axios
    .post(
      'http://localhost:8080/admin/createExercise/',
      {
        name: newExerciseName,
        muscleGroup: muscleGroup
     
      },
      {
        auth: {
          username: storedUser.username,
          password: storedUser.password
        }
      }
    )
    .then(res => {
          console.log("Creating exercise "+newExerciseName)
        console.log(res.data)
          alert("Created exercise :"+deleteExerciseName)
       
})
    .catch(err =>
      console.error('Грешка при добавяне на упражнение:', err)
    );
    
};
const handledeleteExercise = () => {
  axios
    .delete(
      `http://localhost:8080/admin/delete/exercise/${deleteExerciseName}`,
      {
        auth: {
          username: storedUser.username,
          password: storedUser.password
        }
      }
    )
    .then(res => {
          console.log("Removing exercise: "+deleteExerciseName)
      console.log(res.data);
      alert("Removed exercise :"+deleteExerciseName)

    })
    .catch(err =>
      console.error('Грешка при изтриване на упражнение:', err)
    );
};


  return (


        <div className="admin-cards">
            <div className='card'>
     <h2>Add Exercise</h2>
        <p>Name</p>
            <input type="text" 
              value={newExerciseName}
            onChange={(e) => setNewExerciseName(e.target.value)}/>
        <p>Muscle Group</p>
            <input type="text" 
              value={muscleGroup}
            onChange={(e) => setMuscleGroup(e.target.value)}/>
            <button onClick={handleAddExercise}>Add</button>

            </div>

      <div className='card'>
     <h2>Delete Exercise</h2>
        <p>Name</p>
            <input type="text" 
             value={deleteExerciseName}
            onChange={(e) => setDeleteExerciseName(e.target.value)}/>

            <button onClick={handledeleteExercise}>Delete</button>

            </div>

          <div className="card">

                <h2>Add Car</h2>
                <p>Car Name</p>
            <input type="text"
            value={addCarName}
            onChange={(e) => setAddCarName(e.target.value)}
            />
             <p>Car HP</p>
            <input type="text" 
               value={carHP}
            onChange={(e) => setCarHP(e.target.value)}/>
     
              <button onClick={handleAddCar}>Add</button>
        </div>
         <div className="card">

                <h2>Delete Car</h2>
                <p>Car Name</p>
            <input type="text"
            value={deleteCarName}
            onChange={(e) => setDeleteCarName(e.target.value)}
            />
            
              <button onClick={handleDeleteCar}>Delete</button>
        </div>
          <div className="card">

                <h2>Delete User</h2>
                <p>Username</p>
                <input type="text" 
                value={username}
                  onChange={(e) => setUsername(e.target.value)}/>
         
              <button onClick={handleDeleteUser}>Delete</button>
        </div>
        

        </div>
 
  )
}

import { useState, useEffect } from 'react';
import axios from 'axios';
import Popup from './Popup'
import './workout.css';

export default function Workout() {
    const [workoutStarted, setWorkoutStarted] = useState(false);
    const  [newExercisePopup, setNewExercisePopup]= useState(false);
    const [time, setTime] = useState('');
    const [workout, setWorkout] = useState(null);
    const [allExercises, setAllExercises] = useState([]);
    const [showExerciseList, setShowExerciseList] = useState(false);
    const [workoutName, setWorkoutName] = useState('');
const[newExerciseName,setNewExerciseName] = useState('');
const[muscleGroup,setMuscleGroup] = useState('');
    const [workoutExercises, setWorkoutExercises] = useState([]);

    const [message, setMessage] = useState('');

    const storedUser = JSON.parse(localStorage.getItem('user'));
    const token = localStorage.getItem('token');

    useEffect(() => {
        const now = new Date();
        console.log(workout)
        const formattedTime = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        setTime(formattedTime);

        const existingWorkoutId = localStorage.getItem('currentWorkoutId');
        if (existingWorkoutId) {
            setWorkoutStarted(true);
            setWorkout({ data: { workoutId: existingWorkoutId } });
        }
    }, []);
    const groupedExercises = allExercises.reduce((groups, exercise) => {
    const group = exercise.muscleGroup || 'Other';
    if (!groups[group]) {
        groups[group] = [];
    }
    groups[group].push(exercise);
    return groups;
}, {});

    const handleStartWorkout = async () => {
        try {
            const res = await axios.post(
                `${process.env.REACT_APP_API_URL}/api/workout/start/${storedUser.userId}`,
                {},
                {
        headers: {
            'Authorization': `Bearer ${token}` 
        }
    });
            setWorkout(res);
            localStorage.setItem('currentWorkoutId', res.data.workoutId);
            setMessage('Workout started! Add exercises.');
            setWorkoutStarted(true);
            setWorkoutExercises([]);
        } catch (error) {
            console.error('Error starting workout:', error);
            setMessage('Error starting workout. Please try again.');
        }
    };

  const handleFinish = async () => {
    if (!workout || !workout.data || !workout.data.workoutId) {
        setMessage('No active workout to finish.');
        return;
    }

    const workoutId = workout.data.workoutId;

    try {
       
        const exercisePayloads = workoutExercises.map((exercise) => {
            let totalWeight = 0;
            let totalReps = 0;

            exercise.sets.forEach(set => {
                const weight = parseFloat(set.weight) || 0;
                const reps = parseInt(set.reps) || 0;
                totalWeight += weight * reps;
                totalReps += reps;
            });

            return {
                name: exercise.name,
                sets: exercise.sets.length,
                reps: totalReps,
                weight: totalWeight
            };
        });

        
        await axios.post(
            `${process.env.REACT_APP_API_URL}/api/exercises/addToWorkout/${workoutId}`,
            exercisePayloads,
            {
        headers: {
            'Authorization': `Bearer ${token}` 
        }
    });

        
        const res = await axios.post(
            `${process.env.REACT_APP_API_URL}/api/workout/${workoutId}/finish`,
            {
                name:workoutName
            },
            {
        headers: {
            'Authorization': `Bearer ${token}` 
        }
    });

       
        localStorage.removeItem('currentWorkoutId');
        setWorkoutStarted(false);
        setWorkout(null);
        setWorkoutExercises([]);
        setMessage('Workout finished and saved successfully!');
        console.log('Workout complete response:', res.data);
    } catch (error) {
        console.error('Error finishing workout:', error);
        setMessage('An error occurred while saving or finishing workout.');
    }
};
const handleCancel = () => {
    const confirmCancel = window.confirm('Are you sure you want to cancel the current workout? All progress will be lost.');
    
    if (!confirmCancel) return;

    localStorage.removeItem('currentWorkoutId');
    setWorkout(null);
    setWorkoutStarted(false);
    setWorkoutExercises([]);
    setMessage('Workout canceled.');
};




    const handleAddExerciseClick = async () => {
        try {
            const res = await axios.get(`${process.env.REACT_APP_API_URL}/api/exercises`, {
        headers: {
            'Authorization': `Bearer ${token}` 
        }
    })
            setAllExercises(res.data);
            setShowExerciseList(true);
            setMessage('');
        } catch (error) {
            console.error('Error fetching exercises:', error);
            setMessage('Error fetching exercises. Please try again.');
        }
    };

    const handleSelectExerciseFromList = (exercise) => {
        const newExerciseInWorkout = {
            id: Date.now(),
            exerciseId: exercise.id,
            name: exercise.name,
            sets: [{ reps: '', weight: '', completed: false }]
        };
        setWorkoutExercises(prevExercises => [...prevExercises, newExerciseInWorkout]);
        setShowExerciseList(false);
        setMessage(`Added "${exercise.name}". Fill in the sets!`);
    };

    const handleSetChange = (exerciseIndex, setIndex, field, value) => {
        const updatedWorkoutExercises = [...workoutExercises];
        const updatedSets = [...updatedWorkoutExercises[exerciseIndex].sets];
        updatedSets[setIndex] = { ...updatedSets[setIndex], [field]: value };
        updatedWorkoutExercises[exerciseIndex].sets = updatedSets;
        setWorkoutExercises(updatedWorkoutExercises);
    };

    const handleSetCompletedToggle = (exerciseIndex, setIndex) => {
        const updatedWorkoutExercises = [...workoutExercises];
        const updatedSets = [...updatedWorkoutExercises[exerciseIndex].sets];
        updatedSets[setIndex] = { ...updatedSets[setIndex], completed: !updatedSets[setIndex].completed };
        updatedWorkoutExercises[exerciseIndex].sets = updatedSets;
        setWorkoutExercises(updatedWorkoutExercises);
    };


    const handleAddSetToExercise = (exerciseIndex) => {
        const updatedWorkoutExercises = [...workoutExercises];
        updatedWorkoutExercises[exerciseIndex].sets.push({ reps: '', weight: '', completed: false });
        setWorkoutExercises(updatedWorkoutExercises);
    };


    const handleRemoveExercise = (exerciseIndex) => {
        setWorkoutExercises(prevExercises =>
            prevExercises.filter((_, index) => index !== exerciseIndex)
        );
        setMessage('Exercise removed from workout.');
    };

    const handleCreateNewExercise = async () => {
  return axios
    .post(
      `${process.env.REACT_APP_API_URL}/api/users/createExercise/`,
      {
        name: newExerciseName,
        muscleGroup: muscleGroup
     
      },
      {
    headers: {
            'Authorization': `Bearer ${token}` 
        }
      }
    )
    .then(res => {
          console.log("Creating exercise "+newExerciseName)
        console.log(res.data)
       
})
    .catch(err =>
      console.error('Грешка при добавяне на упражнение:', err)
    );
    
};
const  handleBackAndRefreshExercises = async ()  =>{
        setNewExercisePopup(false);
        await handleAddExerciseClick();

}
const  handleSaveAndRefreshExercises = async ()  =>{
    try{
        await handleCreateNewExercise();
        await handleAddExerciseClick();
             setNewExercisePopup(false);
    }catch(err){
        console.error(err);
    }
     
   


}


    return (
        <div className="start-workout-screen">
            {!workoutStarted ? (
                <div >
                    <button className='start-workout-button' onClick={handleStartWorkout}>Start Workout</button>
                </div>
            ) : (
                <div className="workout-active-container">
                    <div className="workout-header-row">
                        <input type="text" 
                        value={workoutName}
                        onChange={(e) => setWorkoutName(e.target.value)}
                        placeholder='Enter Workout Name'/>
                        <span>{time}</span>
                    </div>

                    {message && <p className="message">{message}</p>}

                   {showExerciseList && (
            <>
              <div className="exercise-backdrop" />

              {!newExercisePopup ? (
                
                <div className="exercise-list">    
                    <div className="exercise-list-header">
                    <h3>
                    Select Exercise
                    <button onClick={() => setNewExercisePopup(true)}>New</button></h3>
                    </div>
                    <div className="exercise-list-body">
                    {Object.entries(groupedExercises).map(([group, exs]) => (
                    <div key={group} className='exercise-group'>
                      <h4>{group}</h4>
                      {exs.map(ex => (
                        <button
                          key={ex.id}
                          onClick={() => handleSelectExerciseFromList(ex)}
                        >
                          {ex.name}
                        </button>
                      ))}
                    </div>
                  ))}

                    </div>
                  
                <div className="exercise-list-footer">
                <button onClick={() => setShowExerciseList(false)}>
                    Cancel
                  </button>
                </div>
               
                </div>
              ) : (
                <div className="exercise-list">
                  <h3>New Exercise</h3>

                  <input
                    placeholder="Exercise name"
                    value={newExerciseName}
                    onChange={e => setNewExerciseName(e.target.value)}
                  />

                  <input
                    placeholder="Muscle group"
                    value={muscleGroup}
                    onChange={e => setMuscleGroup(e.target.value)}
                  />

                  <button onClick={handleSaveAndRefreshExercises}>Save</button>
                  <button onClick={handleBackAndRefreshExercises}>
                    Back
                  </button>
                </div>
              )}
            </>
          )}
                    <div className="workout-exercises-grid">
                        {workoutExercises.map((exercise, exerciseIndex) => (
                            <div key={exercise.id} className="exercise-card">
                                <div className="card-header-with-remove">
                                    <h3 className="exercise-name-display">{exercise.name}</h3>
                                    <button className="remove-exercise-btn" onClick={() => handleRemoveExercise(exerciseIndex)}>X</button>
                                </div>

                                <div className="sets-table">
                                    <div className="table-header">
                                        <span className="col-set">Set</span>
                                        <span className="col-weight">kg</span>
                                        <span className="col-reps">Reps</span>
                                        <span className="col-rdy">Rdy</span>
                                    </div>
                                    {exercise.sets.map((set, setIndex) => (
                                        <div key={setIndex} className="set-row">
                                            <span className="col-set">{setIndex + 1}</span>
                                            <input
                                                type="number"
                                                step="0.1"
                                                value={set.weight}
                                                onChange={(e) => handleSetChange(exerciseIndex, setIndex, 'weight', e.target.value)}
                                                placeholder="kg"
                                                className="col-weight"
                                            />
                                            <input
                                                type="number"
                                                value={set.reps}
                                                onChange={(e) => handleSetChange(exerciseIndex, setIndex, 'reps', e.target.value)}
                                                placeholder="reps"
                                                className="col-reps"
                                            />
                                            <input
                                                type="checkbox"
                                                checked={set.completed}
                                                onChange={() => handleSetCompletedToggle(exerciseIndex, setIndex)}
                                                className="col-rdy"
                                            />
                                        </div>
                                    ))}
                                </div>

                                <button className="add-set-button" onClick={() => handleAddSetToExercise(exerciseIndex)}>+ Add Set</button>
                            </div>
                        ))}
                    </div>

                    <div className="global-workout-actions">
                        {!showExerciseList && (
                            <>
                                <button onClick={handleAddExerciseClick}>Add Exercise</button>
                                <button onClick={handleFinish}>Finish Workout</button>
                                 <button onClick={handleCancel}>Cancel</button>
                            </>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}
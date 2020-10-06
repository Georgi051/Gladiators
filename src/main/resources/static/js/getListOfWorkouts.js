function getWorkouts(dayOfWeek){
    let select = document.createElement('select');
    select.classList.add('form-control');
    select.id = 'workout';
    select.name = 'workout';
    let btn;
    let div;
    switch (dayOfWeek) {
        case 'MONDAY':
        btn = document.getElementById('mondayAddBtn');
        div = document.getElementById('mondayWorkoutAdd');
        btn.value = '1';
        break;
        case 'TUESDAY':
            btn = document.getElementById('tuesdayAddBtn');
            div = document.getElementById('tuesdayWorkoutAdd');
            btn.value = '1';
            break;
        case 'WEDNESDAY':
            btn = document.getElementById('wednesdayAddBtn');
            div = document.getElementById('wednesdayWorkoutAdd');
            btn.value = '1';
            break;
        case 'THURSDAY':
            btn = document.getElementById('thursdayAddBtn');
            div = document.getElementById('thursdayWorkoutAdd');
            btn.value = '1';
            break;
        case 'FRIDAY':
            btn = document.getElementById('fridayAddBtn');
            div = document.getElementById('fridayWorkoutAdd');
            btn.value = '1';
            break;
        case 'SATURDAY':
            btn = document.getElementById('saturdayAddBtn');
            div = document.getElementById('saturdayWorkoutAdd');
            btn.value = '1';
            break;
        case 'SUNDAY':
            btn = document.getElementById('sundayAddBtn');
            div = document.getElementById('sundayWorkoutAdd');
            btn.value = '1';
            break;
    }



    if(div.querySelector("select") === null){
    fetch("/api/workouts/all")
        .then((response) => response.json())
        .then((workouts) => {
            workouts.forEach(workout => {
                let option = document.createElement('option');
                option.classList.add("form-check");
                option.value = workout.id;
                option.text = workout.name;
                select.appendChild(option);

            })
        });


        div.appendChild(select);


    }

    if(document.getElementById('mondayAddBtn').value === '1' &&
        document.getElementById('tuesdayAddBtn').value === '1' &&
        document.getElementById('wednesdayAddBtn').value === '1' &&
        document.getElementById('thursdayAddBtn').value === '1' &&
        document.getElementById('fridayAddBtn').value === '1' &&
        document.getElementById('saturdayAddBtn').value === '1' &&
        document.getElementById('sundayAddBtn').value === '1'){
        let createBtn = document.getElementById("createTrainingPlan");
        createBtn.disabled = false;
        div.replaceChild(document.getElementById("createTrainingPlan"), createBtn);
    }
}

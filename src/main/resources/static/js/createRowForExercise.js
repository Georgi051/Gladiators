function createRowForExercise(){


    let table = document.getElementById('tbl');
    let select = document.createElement('select');
    select.id = "exercises";
    select.name = "exercises";
    fetch('/api/exercises/all')
        .then((response) => response.json())
        .then((exercises) => {
            exercises.forEach(exercise => {
                let option = document.createElement('option');
                option.classList.add("form-check");
                option.value = exercise.id;
                option.text = exercise.name;
                select.appendChild(option);
            })
        });

    let tbody = document.createElement("tbody");

    let tr = document.createElement("tr");
    tr.appendChild(select);

        let tdSets = document.createElement("td");

        let inputSets = document.createElement("input");
    inputSets.type = "number";
    inputSets.classList.add("w-40");
    inputSets.name = "sets";
    inputSets.placeholder = "0";
    inputSets.min = "1";
    inputSets.max = "6";
    tdSets.appendChild(inputSets);
        tr.appendChild(tdSets);

    let tdRepeats = document.createElement("td");
    let inputRepeats = document.createElement("input");
    inputRepeats.type = "text";
    inputRepeats.classList.add("w-40");
    inputRepeats.placeholder = "Add repeats";
    inputRepeats.name = "repeats";
    tdRepeats.appendChild(inputRepeats);
    tr.appendChild(tdRepeats);

    let tdRestTime = document.createElement("td");
    let inputRestTime = document.createElement("input");
    inputRestTime.type = "number";
    inputRestTime.classList.add("w-40");
    inputRestTime.name = "restTime";
    inputRestTime.placeholder = "0";
    inputRestTime.min = "1";
    inputRestTime.max = "6";
    tdRestTime.appendChild(inputRestTime);
    tr.appendChild(tdRestTime);

    tbody.appendChild(tr);
    table.appendChild(tbody);



}
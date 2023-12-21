function refresh(row) {
  const table = document.querySelector("#data-table tbody");
  const tr = document.createElement("tr");

  const tdID = document.createElement("td");
  tdID.textContent = row.student_id;

  const tdFname = document.createElement("td");
  tdFname.textContent = row.first_name;

  const tdMname = document.createElement("td");
  tdMname.textContent = row.middle_name;

  const tdLname = document.createElement("td");
  tdLname.textContent = row.last_name;

  const tdDOB = document.createElement("td");
  tdDOB.textContent = row.dob;

  const tdGender = document.createElement("td");
  tdGender.textContent = row.gender;

  const tdCivilStatus = document.createElement("td");
  tdCivilStatus.textContent = row.civil_status;

  const tdPhone = document.createElement("td");
  tdPhone.textContent = row.phone;

  const tdEmail = document.createElement("td");
  tdEmail.textContent = row.email;

  const tdAction = document.createElement("td");

  const deleteIcon = document.createElement("i");
  deleteIcon.className = "fas fa-trash";
  deleteIcon.addEventListener("click", () => deleteStudent(row.student_id));

  const updateIcon = document.createElement("i");
  updateIcon.className = "fas fa-edit";
  updateIcon.addEventListener("click", () => updateStudent(row.student_id));

  tdAction.appendChild(updateIcon);
  tdAction.appendChild(deleteIcon);

  tr.appendChild(tdID);
  tr.appendChild(tdFname);
  tr.appendChild(tdMname);
  tr.appendChild(tdLname);
  tr.appendChild(tdDOB);
  tr.appendChild(tdGender);
  tr.appendChild(tdCivilStatus);
  tr.appendChild(tdPhone);
  tr.appendChild(tdEmail);
  tr.appendChild(tdAction);

  table.appendChild(tr);
}

//function to get JSON data and use it to populate the table
function getData() {
  fetch(`http://localhost:8000/students`)
    .then((response) => response.json())
    .then((data) => {
      data.forEach((row) => {
        refresh(row);
      });
    })
    .catch((error) => {
      console.error("Error: ", error);
    });
}

//function to get JSON data and use it to populate the table
function getData(searchTerm) {
  fetch(`http://localhost:8000/students?q=${searchTerm}`)
    .then((response) => response.json())
    .then((data) => {
      data.forEach((row) => {
        refresh(row);
      });
    })
    .catch((error) => {
      console.error("Error: ", error);
    });
}

//function to handle search event
function search(event) {
  //prevent to leave the page / reload
  event.preventDefault();

  //get search text field value
  const searchTerm = document.getElementById("search-input").value;

  //get a reference to the table tbody
  const table = document.querySelector("#data-table tbody");

  //clear the existing table content
  table.innerHTML = "";

  //fetch data from 'api/search' path
  fetch(`http://localhost:8000/searchStudents?q=${searchTerm}`)
    .then((response) => response.json())
    .then((jsonData) => {
      //populate the table with json data
      jsonData.forEach((row) => {
        refresh(row);
      });
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
    });
}

function save(event) {
  event.preventDefault();

  const table = document.querySelector("#data-table tbody");

  // Get a reference to input fields
  const firstName = document.getElementById("fname");
  const middleName = document.getElementById("mname");
  const lastName = document.getElementById("lname");
  const dateOfBirth = document.getElementById("dob");
  const gender = document.getElementById("gender");
  const civilStatus = document.getElementById("civil_status");
  const phoneNum = document.getElementById("phone");
  const email = document.getElementById("email");

  // Send a POST request to add a new entry
  fetch(`http://localhost:8000/addStudent`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      first_name: firstName.value,
      middle_name: middleName.value,
      last_name: lastName.value,
      dob: dateOfBirth.value,
      gender: gender.value,
      civil_status: civilStatus.value,
      phone: phoneNum.value,
      email: email.value,
    }),
  })
    .then((response) => response.json())
    .then((jsonData) => {
      // Clear the existing table content
      table.innerHTML = "";
      jsonData.forEach((row) => {
        refresh(row);
        // Clear input fields
        firstName.value = "";
        middleName.value = "";
        lastName.value = "";
        dateOfBirth.value = "";
        gender.value = "";
        civilStatus.value = "";
        phoneNum.value = "";
        email.value = "";
      });
      document
        .getElementById("data-table")
        .scrollIntoView({ behavior: "smooth", block: "end" });
    })
    .catch((error) => {
      console.error("Error adding new entry:", error);
    });
}

function deleteStudent(id) {
  const table = document.querySelector("#data-table tbody");
  //confirm dialog box
  const confirm = window.confirm(
    "Are you sure you want to delete this record?"
  );
  if (confirm) {
    const table = document.querySelector("#data-table tbody");
    fetch(`http://localhost:8000/deleteStudent?id=${id}`, {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((jsonData) => {
        table.innerHTML = "";
        jsonData.forEach((row) => {
          refresh(row);
        });
      })
      .catch((error) => {
        console.error("Error deleting the entry", error);
      });
  }
}

function updateStudent(id) {
  fetch(`http://localhost:8000/updateStudent?id=${id}`)
    .then((response) => response.json())
    .then((students) => {
      document.getElementById("edit-save").style.display = "inline-block";
      document.getElementById("new-save").style.display = "none";

      document.getElementById("id").value = students.student_id;
      document.getElementById("fname").value = students.first_name;
      document.getElementById("mname").value = students.middle_name;
      document.getElementById("lname").value = students.last_name;
      document.getElementById("dob").value = students.dob;
      document.getElementById("gender").value = students.gender;
      document.getElementById("civil_status").value = students.civil_status;
      document.getElementById("phone").value = students.phone;
      document.getElementById("email").value = students.email;

      document
        .getElementById("create-form")
        .scrollIntoView({ behavior: "smooth", block: "start" });
      document.getElementById("form-title").textContent =
        "Update Record | " + students.last_name;
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
    });
}

function updateSave(event) {
  event.preventDefault();

  const studentID = document.getElementById("id").value;
  const firstName = document.getElementById("fname").value;
  const middleName = document.getElementById("mname").value;
  const lastName = document.getElementById("lname").value;
  const dateOfBirth = document.getElementById("dob").value;
  const gender = document.getElementById("gender").value;
  const civilStatus = document.getElementById("civil_status").value;
  const phoneNum = document.getElementById("phone").value;
  const email = document.getElementById("email").value;

  const table = document.querySelector("#data-table tbody");

  fetch(`http://localhost:8000/update-save`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      student_id: studentID,
      first_name: firstName,
      middle_name: middleName,
      last_name: lastName,
      dob: dateOfBirth,
      gender: gender,
      civil_status: civilStatus,
      phone: phoneNum,
      email: email,
    }),
  })
    .then((response) => response.json())
    .then((jsonData) => {
      // Clear the existing table content
      table.innerHTML = "";

      // Populate the table with the updated data
      jsonData.forEach((row) => {
        refresh(row);
      });

      // Clear input fields
      document.getElementById("id").value = "";
      document.getElementById("fname").value = "";
      document.getElementById("mname").value = "";
      document.getElementById("lname").value = "";
      document.getElementById("dob").value = "";
      document.getElementById("gender").value = "";
      document.getElementById("civil_status").value = "";
      document.getElementById("phone").value = "";
      document.getElementById("email").value = "";

      document.getElementById("edit-save").style.display = "none";
      document.getElementById("new-save").style.display = "inline-block";

      document
        .getElementById("data-table")
        .scrollIntoView({ behavior: "smooth", block: "start" });
      document.getElementById("form-title").textContent = "Add New Record";
    })
    .catch((error) => {
      console.error("Error updating the entry", error);
    });
}

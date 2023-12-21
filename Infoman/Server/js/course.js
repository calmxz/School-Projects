function refreshCourse(rowC) {
  const table = document.querySelector("#data-table tbody");
  const tr = document.createElement("tr");

  const tdID = document.createElement("td");
  tdID.textContent = rowC.course_id;

  const tdDepartment = document.createElement("td");
  tdDepartment.textContent = rowC.department;

  const tdCourse = document.createElement("td");
  tdCourse.textContent = rowC.course_name;

  const tdTimeStamp = document.createElement("td");
  tdTimeStamp.textContent = rowC.date_created;

  const tdAction = document.createElement("td");

  const deleteIcon = document.createElement("i");
  deleteIcon.className = "fas fa-trash";
  deleteIcon.addEventListener("click", () => deleteRow(rowC.course_id));

  const updateIcon = document.createElement("i");
  updateIcon.className = "fas fa-edit";
  updateIcon.addEventListener("click", () => updateRow(rowC.course_id));

  tdAction.appendChild(updateIcon);
  tdAction.appendChild(deleteIcon);

  tr.appendChild(tdID);
  tr.appendChild(tdDepartment);
  tr.appendChild(tdCourse);
  tr.appendChild(tdTimeStamp);
  tr.appendChild(tdAction);

  table.appendChild(tr);
}

function getCourse() {
  fetch(`http://localhost:8000/courses`)
    .then((response) => response.json())
    .then((data) => {
      data.forEach((rowC) => {
        refreshCourse(rowC);
      });
    })
    .catch((error) => {
      console.error("Error: ", error);
    });
}

function getCourse(searchCourse) {
  fetch(`http://localhost:8000/courses?q=${searchCourse}`)
    .then((response) => response.json())
    .then((data) => {
      data.forEach((rowC) => {
        refreshCourse(rowC);
      });
    })
    .catch((error) => {
      console.error("Error: ", error);
    });
}

function searchCourses(event) {
  event.preventDefault();

  const searchCourse = document.getElementById("searchCourse-input").value;

  const table = document.querySelector("#data-table tbody");

  table.innerHTML = "";

  fetch(`http://localhost:8000/searchCourses?q=${searchCourse}`)
    .then((response) => response.json())
    .then((data) => {
      data.forEach((rowC) => {
        refreshCourse(rowC);
      });
    })
    .catch((error) => {
      console.error("Error fetching data: ", error);
    });
}


function save(event) {
  event.preventDefault();

  const department = document.getElementById("department");
  const course = document.getElementById("course");
  const table = document.querySelector("#data-table tbody");

  const existingCourse = Array.from(table.children).map(row => row.children[2].textContent.toLowerCase());

  if(existingCourse.includes(course.value.toLowerCase())){
    alert("Course already exists!");
    return;
  }

  fetch(`http://localhost:8000/createCourse`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      department: department.value,
      course_name: course.value,
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      table.innerHTML = "";
      data.forEach((rowC) => {
        refreshCourse(rowC);
        department.value = "";
        course.value = "";
      });
      document
        .getElementById("data-table")
        .scrollIntoView({ behavior: "smooth", block: "end" });
    })
    .catch((error) => {
      console.error("Error adding a new entry:", error);
    });
}

function deleteRow(id) {
  const confirm = window.confirm(
    "Are you sure you want to delete this record?"
  );

  if (confirm) {
    const table = document.querySelector("#data-table tbody");
    fetch(`http://localhost:8000/deleteCourse?id=${id}`, {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((jsonData) => {
        table.innerHTML = "";
        jsonData.forEach((rowC) => {
          refreshCourse(rowC);
        });
      })
      .catch((error) => {
        console.error("Error deleting the entry", error);
      });
  }
}

function updateRow(id) {
  fetch(`http://localhost:8000/updateCourse?id=${id}`)
    .then((response) => response.json())
    .then((courses) => {
      document.getElementById("edit-save").style.display = "inline-block";
      document.getElementById("new-save").style.display = "none";

      document.getElementById("id").value = courses.course_id;
      document.getElementById("department").value = courses.department;
      document.getElementById("course").value = courses.course_name;

      document
        .getElementById("create-form")
        .scrollIntoView({ behavior: "smooth", block: "start" });
      document.getElementById("form-title").textContent =
        "Update Record | " + courses.course_name;
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
    });
}

function updateSave(event) {
  event.preventDefault();

  const courseID = document.getElementById("id").value;
  const dept = document.getElementById("department").value;
  const course = document.getElementById("course").value;

  const table = document.querySelector("#data-table tbody");

  fetch(`http://localhost:8000/updateCourse-save`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      course_id: courseID,
      department: dept,
      course_name: course,
    }),
  })
    .then((response) => response.json())
    .then((jsonData) => {
      table.innerHTML = "";

      jsonData.forEach((rowC) => {
        refreshCourse(rowC);
      });

      document.getElementById("id").value = "";
      document.getElementById("department").value = "";
      document.getElementById("course").value = "";

      document.getElementById("edit-save").style.display = "none";
      document.getElementById("new-save").style.display = "inline-block";

      document
        .getElementById("data-table")
        .scrollIntoView({ behavior: "smooth", block: "start" });
      document.getElementById("form-title").textContent = "Add New Course";
    })
    .catch((error) => {
      console.error("Error updating the entry", error);
    });
}

//load data after the page is loaded
window.addEventListener("DOMContentLoaded", loadStudent);

//loads course to content div
function loadCourse(event) {
  if (event) {
    event.preventDefault();
  }

  document.querySelector("li.active").classList.remove("active");

  document.getElementById("course-nav").classList.add("active");

  var content = document.querySelector(".content");

  fetch(`/content-courses`)
    .then((response) => response.text())
    .then((html) => {
      content.innerHTML = html;

      const mainJS = document.querySelector("script[src='/js']");

      mainJS.parentNode.removeChild(mainJS);

      const courseJS = document.createElement("script");

      courseJS.src = "/js-course";

      document.body.appendChild(courseJS);
      document.body.appendChild(mainJS);

      courseJS.onload = function () {
        getCourse();
      };
    })
    .catch((error) => {
      console.error("Error", error);
    });
}

//loads student to content div
function loadStudent(event) {
  if (event) {
    event.preventDefault();
  }

  document.querySelector("li.active").classList.remove("active");

  document.getElementById("student-nav").classList.add("active");
  var content = document.querySelector(".content");

  fetch(`/content-students`)
    .then((response) => response.text())
    .then((html) => {
      content.innerHTML = html;

      const mainJS = document.querySelector("script[src='/js']");

      mainJS.parentNode.removeChild(mainJS);

      const studentJS = document.createElement("script");

      studentJS.src = "/js-student";

      document.body.appendChild(studentJS);
      document.body.appendChild(mainJS);

      studentJS.onload = function () {
        getData();
      };
    })
    .catch((error) => {
      console.error("Error", error);
    });
}

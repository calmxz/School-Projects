import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.*;

public class App {

  public static void main(String[] args) throws Exception {
    String url = "jdbc:mariadb://localhost:3306/college_db";
    String user = "root";
    String password = "1234";

    try {
      int port = 8000;
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

      server.createContext(
        "/students",
        exchange -> {
          String json = "[\n";
          try {
            Connection db = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM students";

            PreparedStatement ps = db.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
              int id = rs.getInt("student_id");
              String fName = rs.getString("first_name");
              String mName = rs.getString("middle_name");
              String lName = rs.getString("last_name");
              String dob = rs.getString("dob");
              String gender = rs.getString("gender");
              String civilStatus = rs.getString("civil_status");
              String phone = rs.getString("phone");
              String email = rs.getString("email");

              json += "{";
              json += "\"student_id\": \"" + id + "\"";
              json += ",\"first_name\": \"" + fName + "\"";
              json += ",\"middle_name\": \"" + mName + "\"";
              json += ",\"last_name\": \"" + lName + "\"";
              json += ",\"dob\": \"" + dob + "\"";
              json += ",\"gender\": \"" + gender + "\"";
              json += ",\"civil_status\": \"" + civilStatus + "\"";
              json += ",\"phone\": \"" + phone + "\"";
              json += ",\"email\": \"" + email + "\"";
              json += "}";

              if (!rs.isLast()) {
                json += ",\n";
              }
            }

            json += "\n]";
          } catch (Exception e) {
            throw new RuntimeException(e);
          }

          exchange.getResponseHeaders().set("Content-Type", "application/json");
          exchange.sendResponseHeaders(200, json.length());

          OutputStream output = exchange.getResponseBody();
          output.write(json.getBytes());
          output.close();
        }
      );

      server.createContext(
        "/courses",
        exchange -> {
          String jsonCourse = "[\n";
          try {
            Connection db = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM course";

            PreparedStatement ps = db.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
              int id = rs.getInt("course_id");
              String department = rs.getString("department");
              String course = rs.getString("course_name");
              String dateCreated = rs.getString("date_created");

              jsonCourse += "{";
              jsonCourse += "\"course_id\": \"" + id + "\"";
              jsonCourse += ",\"department\": \"" + department + "\"";
              jsonCourse += ",\"course_name\": \"" + course + "\"";
              jsonCourse += ",\"date_created\": \"" + dateCreated + "\"";
              jsonCourse += "}";

              if (!rs.isLast()) {
                jsonCourse += ",\n";
              }
            }

            jsonCourse += "\n]";
          } catch (Exception e) {
            throw new RuntimeException(e);
          }

          exchange.getResponseHeaders().set("Content-Type", "application/json");
          exchange.sendResponseHeaders(200, jsonCourse.length());

          OutputStream output = exchange.getResponseBody();
          output.write(jsonCourse.getBytes());
          output.close();
        }
      );

      server.createContext(
        "/",
        exchange -> {
          byte[] htmlContent = Files.readAllBytes(Paths.get("html/index.html"));

          exchange.getResponseHeaders().set("Content-Type", "text/html");
          exchange.sendResponseHeaders(200, htmlContent.length);

          OutputStream output = exchange.getResponseBody();
          output.write(htmlContent);
          output.close();
        }
      );

      server.createContext(
        "/content-students",
        exchange -> {
          byte[] htmlContent = Files.readAllBytes(
            Paths.get("html/students.html")
          );

          exchange.getResponseHeaders().set("Content-Type", "text/html");
          exchange.sendResponseHeaders(200, htmlContent.length);

          OutputStream output = exchange.getResponseBody();
          output.write(htmlContent);
          output.close();
        }
      );

      server.createContext(
        "/content-courses",
        exchange -> {
          byte[] htmlContent = Files.readAllBytes(
            Paths.get("html/courses.html")
          );

          exchange.getResponseHeaders().set("Content-Type", "text/html");
          exchange.sendResponseHeaders(200, htmlContent.length);

          OutputStream output = exchange.getResponseBody();
          output.write(htmlContent);
          output.close();
        }
      );

      // create a context for serving the CSS
      server.createContext(
        "/css",
        exchange -> {
          byte[] htmlContent = Files.readAllBytes(Paths.get("css/style.css"));

          exchange.getResponseHeaders().set("Content-Type", "text/css");
          exchange.sendResponseHeaders(200, htmlContent.length);

          OutputStream output = exchange.getResponseBody();
          output.write(htmlContent);
          output.close();
        }
      );

      //create context -> js
      server.createContext(
        "/js",
        exchange -> {
          byte[] htmlContent = Files.readAllBytes(Paths.get("js/app.js"));

          exchange.getResponseHeaders().set("Content-Type", "text/javascript");
          exchange.sendResponseHeaders(200, htmlContent.length);

          OutputStream output = exchange.getResponseBody();
          output.write(htmlContent);
          output.close();
        }
      );

      server.createContext(
        "/js-student",
        exchange -> {
          byte[] htmlContent = Files.readAllBytes(Paths.get("js/student.js"));

          exchange.getResponseHeaders().set("Content-Type", "text/javascript");
          exchange.sendResponseHeaders(200, htmlContent.length);

          OutputStream output = exchange.getResponseBody();
          output.write(htmlContent);
          output.close();
        }
      );

      server.createContext(
        "/js-course",
        exchange -> {
          byte[] htmlContent = Files.readAllBytes(Paths.get("js/course.js"));

          exchange.getResponseHeaders().set("Content-Type", "text/javascript");
          exchange.sendResponseHeaders(200, htmlContent.length);

          OutputStream output = exchange.getResponseBody();
          output.write(htmlContent);
          output.close();
        }
      );

      server.createContext(
        "/searchStudents",
        exchange -> {
          // this will remove the "q=" and retain only the value
          // for instance, if you searched "apple", the request will include query
          // parameter - q=apple
          String keyword = exchange.getRequestURI().getQuery().substring(2);

          // create a StringBuilder object for the JSON
          StringBuilder jsonData = new StringBuilder();
          jsonData.append("[\n"); // start an array of JSON objects

          try {
            // initialize db connection
            Connection dbConnection = DriverManager.getConnection(
              url,
              user,
              password
            );

            // SQL query to read data with filter
            String sql =
              "SELECT * FROM students WHERE first_name LIKE ? OR middle_name LIKE ? OR last_name LIKE ? OR dob LIKE ? OR gender LIKE ? OR civil_status LIKE ? OR phone LIKE ? OR email LIKE ?";

            // create a PreparedStatement
            PreparedStatement preparedStatement = dbConnection.prepareStatement(
              sql
            );

            // fill in the SQL parameters ? with actual value
            preparedStatement.setString(1, "%" + keyword + "%"); // first_name
            preparedStatement.setString(2, "%" + keyword + "%"); // middle_name
            preparedStatement.setString(3, "%" + keyword + "%"); // last_name
            preparedStatement.setString(4, "%" + keyword + "%"); // dob
            preparedStatement.setString(5, "%" + keyword + "%"); // gender
            preparedStatement.setString(6, "%" + keyword + "%"); // civil_status
            preparedStatement.setString(7, "%" + keyword + "%"); // phone
            preparedStatement.setString(8, "%" + keyword + "%"); // email

            // execute the query and get the result
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
              int id = resultSet.getInt("student_id");
              String fName = resultSet.getString("first_name");
              String mName = resultSet.getString("middle_name");
              String lName = resultSet.getString("last_name");
              String dob = resultSet.getString("dob");
              String gender = resultSet.getString("gender");
              String civilStatus = resultSet.getString("civil_status");
              String phone = resultSet.getString("phone");
              String email = resultSet.getString("email");

              jsonData.append("{");
              jsonData.append("\"student_id\": \"" + id + "\"");
              jsonData.append(",\"first_name\": \"" + fName + "\"");
              jsonData.append(",\"middle_name\": \"" + mName + "\"");
              jsonData.append(",\"last_name\": \"" + lName + "\"");
              jsonData.append(",\"dob\": \"" + dob + "\"");
              jsonData.append(",\"gender\": \"" + gender + "\"");
              jsonData.append(",\"civil_status\": \"" + civilStatus + "\"");
              jsonData.append(",\"phone\": \"" + phone + "\"");
              jsonData.append(",\"email\": \"" + email + "\"");
              jsonData.append("}");

              if (!resultSet.isLast()) {
                jsonData.append(",\n");
              }
            }
            jsonData.append("\n]");
          } catch (SQLException sqlE) {
            sqlE.printStackTrace();
          }

          // set response headers and write the JSON response
          exchange.getResponseHeaders().set("Content-Type", "application/json");
          exchange.sendResponseHeaders(200, jsonData.length());
          OutputStream os = exchange.getResponseBody();
          os.write(jsonData.toString().getBytes());
          os.close();
        }
      );

      server.createContext(
        "/searchCourses",
        exchange -> {
          String keyword = exchange.getRequestURI().getQuery().substring(2);

          StringBuilder jsonCourse = new StringBuilder();
          jsonCourse.append("[\n");

          try {
            Connection dbConnection = DriverManager.getConnection(
              url,
              user,
              password
            );

            String sql =
              "SELECT * FROM course WHERE department LIKE ? OR course_name LIKE ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(
              sql
            );

            preparedStatement.setString(1, "%" + keyword + "%"); //dept
            preparedStatement.setString(2, "%" + keyword + "%"); //course_name

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
              int id = resultSet.getInt("course_id");
              String department = resultSet.getString("department");
              String courseName = resultSet.getString("course_name");
              String dateCreated = resultSet.getString("date_created");

              jsonCourse.append("{");
              jsonCourse.append("\"course_id\": \"" + id + "\"");
              jsonCourse.append(",\"department\": \"" + department + "\"");
              jsonCourse.append(",\"course_name\": \"" + courseName + "\"");
              jsonCourse.append(",\"date_created\": \"" + dateCreated + "\"");
              jsonCourse.append("}");

              if (!resultSet.isLast()) {
                jsonCourse.append(",\n");
              }
            }
            jsonCourse.append("\n]");
          } catch (SQLException sqlE) {
            sqlE.printStackTrace();
          }

          exchange.getResponseHeaders().set("Content-Type", "application/json");
          exchange.sendResponseHeaders(200, jsonCourse.length());
          OutputStream os = exchange.getResponseBody();
          os.write(jsonCourse.toString().getBytes());
          os.close();
        }
      );

      server.createContext(
        "/addStudent",
        exchange -> {
          if ("POST".equals(exchange.getRequestMethod())) {
            try {
              InputStreamReader isr = new InputStreamReader(
                exchange.getRequestBody(),
                "utf-8"
              );
              BufferedReader br = new BufferedReader(isr);
              StringBuilder requestBody = new StringBuilder();
              String line;
              while ((line = br.readLine()) != null) {
                requestBody.append(line);
              }
              JSONObject json = new JSONObject(requestBody.toString());
              String firstName = json.getString("first_name");
              String middleName = json.getString("middle_name");
              String lastName = json.getString("last_name");
              String dob = json.getString("dob");
              String gender = json.getString("gender");
              String civilStatus = json.getString("civil_status");
              String phone = json.getString("phone");
              String email = json.getString("email");

              Connection dbConnection = DriverManager.getConnection(
                url,
                user,
                password
              );
              String sql =
                "INSERT INTO students (first_name, middle_name, last_name, dob, gender, civil_status, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
              PreparedStatement preparedStatement = dbConnection.prepareStatement(
                sql
              );

              preparedStatement.setString(1, firstName);
              preparedStatement.setString(2, middleName);
              preparedStatement.setString(3, lastName);
              preparedStatement.setString(4, dob);
              preparedStatement.setString(5, gender);
              preparedStatement.setString(6, civilStatus);
              preparedStatement.setString(7, phone);
              preparedStatement.setString(8, email);

              preparedStatement.executeUpdate();

              String updateSQL = "SELECT * FROM students";
              PreparedStatement ps = dbConnection.prepareStatement(updateSQL);

              ResultSet rs = ps.executeQuery();

              String updatedJson = "[\n";

              while (rs.next()) {
                int id = rs.getInt("student_id");
                String fName = rs.getString("first_name");
                String mName = rs.getString("middle_name");
                String lName = rs.getString("last_name");
                String dateOfBirth = rs.getString("dob");
                String genderFM = rs.getString("gender");
                String civilstatus = rs.getString("civil_status");
                String phoneNumber = rs.getString("phone");
                String emailAddress = rs.getString("email");

                updatedJson += "{";
                updatedJson += "\"student_id\": \"" + id + "\"";
                updatedJson += ",\"first_name\": \"" + fName + "\"";
                updatedJson += ",\"middle_name\": \"" + mName + "\"";
                updatedJson += ",\"last_name\": \"" + lName + "\"";
                updatedJson += ",\"dob\": \"" + dateOfBirth + "\"";
                updatedJson += ",\"gender\": \"" + genderFM + "\"";
                updatedJson += ",\"civil_status\": \"" + civilstatus + "\"";
                updatedJson += ",\"phone\": \"" + phoneNumber + "\"";
                updatedJson += ",\"email\": \"" + emailAddress + "\"";
                updatedJson += "}";

                if (!rs.isLast()) {
                  updatedJson += ",\n";
                }
              }

              updatedJson += "\n]";

              exchange
                .getResponseHeaders()
                .set("Content-Type", "application/json");
              exchange.sendResponseHeaders(200, updatedJson.length());

              OutputStream output = exchange.getResponseBody();
              output.write(updatedJson.getBytes());
              output.close();
            } catch (Exception e) {
              e.printStackTrace();
              String response = "Error adding a new entry";
              exchange.getResponseHeaders().set("Content-Type", "text/plain");
              exchange.sendResponseHeaders(500, response.length());
              OutputStream os = exchange.getResponseBody();
              os.write(response.getBytes());
              os.close();
            }
          } else {
            String response = "Invalid request";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
          }
        }
      );

      server.createContext(
        "/createCourse",
        exchange -> {
          if ("POST".equals(exchange.getRequestMethod())) {
            try {
              InputStreamReader isr = new InputStreamReader(
                exchange.getRequestBody(),
                "utf-8"
              );
              BufferedReader br = new BufferedReader(isr);
              StringBuilder requestBody = new StringBuilder();

              String line;
              while ((line = br.readLine()) != null) {
                requestBody.append(line);
              }

              JSONObject jsonCourse = new JSONObject(requestBody.toString());
              String department = jsonCourse.getString("department");
              String course = jsonCourse.getString("course_name");

              Connection db = DriverManager.getConnection(url, user, password);

              String sql =
                "INSERT INTO course (department, course_name) VALUES (?, ?)";

              PreparedStatement ps = db.prepareStatement(sql);

              ps.setString(1, department);
              ps.setString(2, course);

              ps.executeUpdate();

              String updateSQL = "SELECT * FROM course";
              PreparedStatement preparedStatement = db.prepareStatement(
                updateSQL
              );

              ResultSet rs = preparedStatement.executeQuery();

              String updatedJsonCourse = "[\n";

              while (rs.next()) {
                int id = rs.getInt("course_id");
                String dept = rs.getString("department");
                String courseName = rs.getString("course_name");
                String dateCreated = rs.getString("date_created");

                updatedJsonCourse += "{";
                updatedJsonCourse += "\"course_id\": \"" + id + "\"";
                updatedJsonCourse += ",\"department\": \"" + dept + "\"";
                updatedJsonCourse += ",\"course_name\": \"" + courseName + "\"";
                updatedJsonCourse +=
                  ",\"date_created\": \"" + dateCreated + "\"";
                updatedJsonCourse += "}";

                if (!rs.isLast()) {
                  updatedJsonCourse += ",\n";
                }
              }

              updatedJsonCourse += "\n]";

              exchange
                .getResponseHeaders()
                .set("Content-Type", "application/json");
              exchange.sendResponseHeaders(200, updatedJsonCourse.length());

              OutputStream os = exchange.getResponseBody();
              os.write(updatedJsonCourse.getBytes());
              os.close();
            } catch (Exception e) {
              e.printStackTrace();
              String response = "Error adding a new entry";
              exchange.getResponseHeaders().set("Content-Type", "text/plain");
              exchange.sendResponseHeaders(500, response.length());
              OutputStream os = exchange.getResponseBody();
              os.write(response.getBytes());
              os.close();
            }
          } else {
            String response = "Invalid request";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
          }
        }
      );

      server.createContext(
        "/deleteStudent",
        exchange -> {
          if ("DELETE".equals(exchange.getRequestMethod())) {
            String id = exchange.getRequestURI().getQuery().substring(3);

            try {
              Connection dbConnection = DriverManager.getConnection(
                url,
                user,
                password
              );

              String sql = "DELETE from students WHERE student_id = ?";

              PreparedStatement preparedStatement = dbConnection.prepareStatement(
                sql
              );
              preparedStatement.setInt(1, Integer.parseInt(id));

              preparedStatement.executeUpdate();

              sql = "SELECT * FROM students";
              preparedStatement = dbConnection.prepareStatement(sql);

              ResultSet resultSet = preparedStatement.executeQuery();

              JSONArray jsonArr = new JSONArray();

              while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("student_id", resultSet.getInt("student_id"));
                jsonObject.put("first_name", resultSet.getString("first_name"));
                jsonObject.put(
                  "middle_name",
                  resultSet.getString("middle_name")
                );
                jsonObject.put("last_name", resultSet.getString("last_name"));
                jsonObject.put("dob", resultSet.getString("dob"));
                jsonObject.put("gender", resultSet.getString("gender"));
                jsonObject.put(
                  "civil_status",
                  resultSet.getString("civil_status")
                );
                jsonObject.put("phone", resultSet.getString("phone"));
                jsonObject.put("email", resultSet.getString("email"));
                jsonArr.put(jsonObject);
              }

              String jsonString = jsonArr.toString();

              preparedStatement.close();

              dbConnection.close();

              exchange
                .getResponseHeaders()
                .set("Content-Type", "application/json");
              exchange.sendResponseHeaders(200, jsonString.length());
              OutputStream os = exchange.getResponseBody();
              os.write(jsonString.getBytes());
              os.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else {
            String response =
              "Invalid HTTP method. Only DELETE requests are accepted";
            exchange.sendResponseHeaders(405, response.length());
            exchange.getResponseBody().write(response.getBytes());
          }
        }
      );

      server.createContext(
        "/deleteCourse",
        exchange -> {
          if ("DELETE".equals(exchange.getRequestMethod())) {
            String id = exchange.getRequestURI().getQuery().substring(3);

            try {
              Connection db = DriverManager.getConnection(url, user, password);

              String sql = "DELETE from course WHERE course_id = ?";

              PreparedStatement ps = db.prepareStatement(sql);

              ps.setInt(1, Integer.parseInt(id));
              ps.executeUpdate();

              sql = "SELECT * FROM course";
              ps = db.prepareStatement(sql);

              ResultSet rs = ps.executeQuery();

              JSONArray jsonCourse = new JSONArray();

              while (rs.next()) {
                JSONObject jsonCourseObj = new JSONObject();
                jsonCourseObj.put("course_id", rs.getInt("course_id"));
                jsonCourseObj.put("department", rs.getString("department"));
                jsonCourseObj.put("course_name", rs.getString("course_name"));
                jsonCourseObj.put("date_created", rs.getString("date_created"));
                jsonCourse.put(jsonCourseObj);
              }

              String jsonString = jsonCourse.toString();

              ps.close();
              db.close();

              exchange
                .getResponseHeaders()
                .set("Content-Type", "application/json");
              exchange.sendResponseHeaders(200, jsonString.length());

              OutputStream os = exchange.getResponseBody();
              os.write(jsonString.getBytes());
              os.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else {
            String response =
              "Invalid HTP method. Only DELETE requests are accepted";
            exchange.sendResponseHeaders(405, response.length());
            exchange.getResponseBody().write(response.getBytes());
          }
        }
      );

      server.createContext(
        "/updateStudent",
        exchange -> {
          String id = exchange.getRequestURI().getQuery().substring(3);

          try {
            Connection dbConnection = DriverManager.getConnection(
              url,
              user,
              password
            );

            String sql = "SELECT * FROM students WHERE student_id = ?";

            PreparedStatement preparedStatement = dbConnection.prepareStatement(
              sql
            );
            preparedStatement.setInt(1, Integer.parseInt(id));

            ResultSet resultSet = preparedStatement.executeQuery();
            JSONObject jsonObject = new JSONObject();

            while (resultSet.next()) {
              jsonObject.put("student_id", resultSet.getInt("student_id"));
              jsonObject.put("first_name", resultSet.getString("first_name"));
              jsonObject.put("middle_name", resultSet.getString("middle_name"));
              jsonObject.put("last_name", resultSet.getString("last_name"));
              jsonObject.put("dob", resultSet.getString("dob"));
              jsonObject.put("gender", resultSet.getString("gender"));
              jsonObject.put(
                "civil_status",
                resultSet.getString("civil_status")
              );
              jsonObject.put("phone", resultSet.getString("phone"));
              jsonObject.put("email", resultSet.getString("email"));
            }

            String jsonString = jsonObject.toString();
            preparedStatement.close();
            dbConnection.close();

            exchange
              .getResponseHeaders()
              .set("Content-Type", "application/json");
            exchange.sendResponseHeaders(20, jsonString.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonString.getBytes());
            os.close();
          } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
          }
        }
      );

      server.createContext(
        "/updateCourse",
        exchange -> {
          String id = exchange.getRequestURI().getQuery().substring(3);

          try {
            Connection db = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM course where course_id = ?";

            PreparedStatement preparedStatement = db.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(id));

            ResultSet resultSet = preparedStatement.executeQuery();
            JSONObject jsonCourseObj = new JSONObject();

            while (resultSet.next()) {
              jsonCourseObj.put("course_id", resultSet.getInt("course_id"));
              jsonCourseObj.put(
                "department",
                resultSet.getString("department")
              );
              jsonCourseObj.put(
                "course_name",
                resultSet.getString("course_name")
              );
              jsonCourseObj.put(
                "date_created",
                resultSet.getString("date_created")
              );
            }

            String jsonString = jsonCourseObj.toString();
            preparedStatement.close();
            db.close();

            exchange
              .getResponseHeaders()
              .set("Content-Type", "application/json");
            exchange.sendResponseHeaders(20, jsonString.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonString.getBytes());
            os.close();
          } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
          }
        }
      );

      server.createContext(
        "/updateCourse-save",
        exchange -> {
          if ("PUT".equals(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(
              exchange.getRequestBody(),
              "utf-8"
            );
            BufferedReader br = new BufferedReader(isr);
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
              requestBody.append(line);
            }

            try {
              JSONObject jsonCourse = new JSONObject(requestBody.toString());
              String department = jsonCourse.getString("department");
              String course = jsonCourse.getString("course_name");
              int id = jsonCourse.getInt("course_id");

              Connection db = DriverManager.getConnection(url, user, password);

              String sql =
                "UPDATE course SET department = ?, course_name = ? WHERE course_id = ?";

              PreparedStatement preparedStatement = db.prepareStatement(sql);
              preparedStatement.setString(1, department);
              preparedStatement.setString(2, course);
              preparedStatement.setInt(3, id);

              preparedStatement.executeUpdate();

              String updateSQL = "SELECT * FROM course";
              PreparedStatement ps = db.prepareStatement(updateSQL);

              ResultSet rs = ps.executeQuery();

              String updatedJsonCourse = "[\n";

              while (rs.next()) {
                int courseID = rs.getInt("course_id");
                String dept = rs.getString("department");
                String courseName = rs.getString("course_name");
                String dateCreated = rs.getString("date_created");

                updatedJsonCourse += "{";
                updatedJsonCourse += "\"course_id\": \"" + courseID + "\"";
                updatedJsonCourse += ",\"department\": \"" + dept + "\"";
                updatedJsonCourse += ",\"course_name\": \"" + courseName + "\"";
                updatedJsonCourse +=
                  ",\"date_created\": \"" + dateCreated + "\"";
                updatedJsonCourse += "}";

                if (!rs.isLast()) {
                  updatedJsonCourse += ",\n";
                }
              }

              updatedJsonCourse += "\n]";

              exchange
                .getResponseHeaders()
                .set("Content-Type", "application/json");
              exchange.sendResponseHeaders(200, updatedJsonCourse.length());

              OutputStream output = exchange.getResponseBody();
              output.write(updatedJsonCourse.getBytes());
              output.close();
            } catch (Exception e) {
              e.printStackTrace();

              String response = "Error adding a new entry";
              exchange.getResponseHeaders().set("Content-Type", "text/plain");
              exchange.sendResponseHeaders(500, response.length());

              OutputStream os = exchange.getResponseBody();
              os.write(response.getBytes());
              os.close();
            }
          } else {
            String response = "Method not allowed";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
          }
        }
      );

      server.createContext(
        "/update-save",
        exchange -> {
          if ("PUT".equals(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(
              exchange.getRequestBody(),
              "utf-8"
            );
            BufferedReader br = new BufferedReader(isr);
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
              requestBody.append(line);
            }

            try {
              JSONObject json = new JSONObject(requestBody.toString());
              String firstName = json.getString("first_name");
              String middleName = json.getString("middle_name");
              String lastName = json.getString("last_name");
              String dob = json.getString("dob");
              String gender = json.getString("gender");
              String civilStatus = json.getString("civil_status");
              String phone = json.getString("phone");
              String email = json.getString("email");
              int id = json.getInt("student_id");

              Connection dbConnection = DriverManager.getConnection(
                url,
                user,
                password
              );

              String sql =
                "UPDATE students SET first_name = ?, middle_name = ?, last_name = ?, " +
                "dob = ?, gender = ?, civil_status = ?, phone = ?, email = ? WHERE student_id = ?";

              PreparedStatement preparedStatement = dbConnection.prepareStatement(
                sql
              );
              preparedStatement.setString(1, firstName);
              preparedStatement.setString(2, middleName);
              preparedStatement.setString(3, lastName);
              preparedStatement.setString(4, dob);
              preparedStatement.setString(5, gender);
              preparedStatement.setString(6, civilStatus);
              preparedStatement.setString(7, phone);
              preparedStatement.setString(8, email);
              preparedStatement.setInt(9, id);

              preparedStatement.executeUpdate();

              String updateSQL = "SELECT * FROM students";
              PreparedStatement ps = dbConnection.prepareStatement(updateSQL);

              ResultSet rs = ps.executeQuery();

              String updatedJson = "[\n";

              while (rs.next()) {
                int ID = rs.getInt("student_id");
                String fName = rs.getString("first_name");
                String mName = rs.getString("middle_name");
                String lName = rs.getString("last_name");
                String dateOfBirth = rs.getString("dob");
                String genderFM = rs.getString("gender");
                String civilstatus = rs.getString("civil_status");
                String phoneNumber = rs.getString("phone");
                String emailAddress = rs.getString("email");

                updatedJson += "{";
                updatedJson += "\"student_id\": \"" + ID + "\"";
                updatedJson += ",\"first_name\": \"" + fName + "\"";
                updatedJson += ",\"middle_name\": \"" + mName + "\"";
                updatedJson += ",\"last_name\": \"" + lName + "\"";
                updatedJson += ",\"dob\": \"" + dateOfBirth + "\"";
                updatedJson += ",\"gender\": \"" + genderFM + "\"";
                updatedJson += ",\"civil_status\": \"" + civilstatus + "\"";
                updatedJson += ",\"phone\": \"" + phoneNumber + "\"";
                updatedJson += ",\"email\": \"" + emailAddress + "\"";
                updatedJson += "}";

                if (!rs.isLast()) {
                  updatedJson += ",\n";
                }
              }

              updatedJson += "\n]";

              exchange
                .getResponseHeaders()
                .set("Content-Type", "application/json");
              exchange.sendResponseHeaders(200, updatedJson.length());

              OutputStream output = exchange.getResponseBody();
              output.write(updatedJson.getBytes());
              output.close();
            } catch (Exception e) {
              e.printStackTrace();

              String response = "Error adding a new entry.";
              exchange.getResponseHeaders().set("Content-Type", "text/plain");
              exchange.sendResponseHeaders(500, response.length());
              OutputStream os = exchange.getResponseBody();
              os.write(response.getBytes());
              os.close();
            }
          } else {
            String response = "Method not allowed";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
          }
        }
      );

      // start server
      server.setExecutor(null);
      server.start();
      System.out.println("The server is running at http://localhost:" + port);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
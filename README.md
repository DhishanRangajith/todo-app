## Getting Started

### 1. Clone the Repository

Clone this repository to your local machine:

```bash
git clone https://github.com/DhishanRangajith/todo-app.git
cd todo-app
```

### 2. Build the Backend WAR File (if needed)

If you need to rebuild the backend WAR file, navigate to the `todo-app` directory and run the following Maven command:

```bash
mvn clean package
```

This will generate the WAR file in the `target/` folder.

### 3. Run the Application Using Docker Compose

To run the application with Docker Compose, execute the following command from the project root:

```bash
docker-compose up --build
```

This command will:

* Build the Docker images for both the frontend (Angular) and backend (Spring Boot) applications.
* Start the containers for the frontend, backend, and MySQL services.
* Map the following ports:

  * Frontend: http://localhost:4300
  * Backend: http://localhost:8020
  * MySQL: localhost:3319

### 4. Wait for the Application to Start

It may take a few minutes for all the containers to be built and started. The MySQL service will need to initialize, and once it’s up, the backend will start automatically.

> Note: Docker Compose ensures that the backend service starts only when MySQL is ready.

### 5. Access the Application

Once everything is running, you can access the following:

* **Frontend:** The Angular application will be available at http://localhost:4300 in your web browser.
* **Backend:** The Spring Boot backend API will be available at http://localhost:8020.
* **MySQL:** The MySQL database will be accessible at `localhost:3319` with the following credentials:

  * Username: dhishan
  * Password: 1234
  * Database: todo_app

You can use a MySQL client to connect or simply use the backend application to interact with the database.

### 6. Stopping the Containers

To stop the running containers, use the following command:

```bash
docker-compose down
```

### 7. Code Coverage with Maven

To check code coverage and ensure that it meets the required threshold (default set to 80%), you can update the Maven `pom.xml` file and run the following commands:

* **Verify the code coverage threshold:**

```bash
mvn verify
```

* **Run unit tests and generate the JaCoCo code coverage report:**

```bash
mvn test
```

The coverage report will be generated in the path: `/todo-app/target/site/index.html`

---

### 8. Frontend Unit Testing

Note: Frontend unit tests are currently pending and need to be implemented for the Angular application. Once these tests are ready, you can run them using Angular's testing tools like Jasmine and Karma.


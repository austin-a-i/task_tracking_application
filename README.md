# task_tracking_application
 Task Tracking Application :  - Build a task tracking application for managing tasks and assignments. - Implement CRUD operations to create, view, update, and delete tasks. 

--------------------------------------------------------
Current Status   
-------------------------------------------------------
1. Setup: 
○ Created a new Spring Boot project using Spring Initializr using Java 21 and maven.
○ Included dependencies for Spring Web, Spring Security, Spring Data JPA, and MySQL Driver.

2. Database Setup:     
○ Created a Mysql database - tasktracker_db.
○ Create tables for user authentication (e.g., users, rolesTables).
○ Created Table to store task data such as task name, description, due date, and status.

3. Entity and Repository:
○ Create entity classes to represent domain entities.       
○ Implement repository interfaces to perform CRUD operations.

4. Controller: 
○ Develop RESTful APIs to perform CRUD operations on domain entities.

--------------------
Yet to complete      
---------------------

REST APIs for Task        
Configured Spring Security for authenication (Bypassed endpoints to createUser and login)       
Implement authentication and authorization mechanisms to secure the APIs.      
Unit Testing     


 ------------------------------------------
API Endpoints    
-------------------------------------------

/user/register - For Adding User   

 curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'	{
	        "username":"austin14",
	        "email":"austin14@gmail.com",
	        "password":"1234"
	}' \
 'http://localhost:8080/user/register'     



 /user/login - For logging into useraccount

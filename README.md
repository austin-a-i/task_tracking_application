# task_tracking_application      

 Task Tracking Application :  -     
Built a task tracking application for managing tasks and implemented CRUD operations to create, view, update, and delete tasks for the users.     

--------------------------------------------------------
Requirements completed      
-------------------------------------------------------     

1. Setup: 
○ Created a new Spring Boot project using Spring Initializr using Java 21 and maven.      
○ Included dependencies for Spring Web, Spring Security, Spring Data JPA, and MySQL Driver.    
  
2. Database Setup:     
○ Created a Mysql database - tasktracker_db.    
○ Created tables for user authentication (e.g., users, authorities).    
○ Created Table to store task data such as task name, description, due date, and status.    

3. Entity and Repository:    
○ Created entity classes to represent domain entities.          
○ Implemented repository interfaces to perform CRUD operations.    

4. Controller:    
○ Developed RESTful APIs to perform CRUD operations on domain entities.    
○ Implemented authentication and authorization mechanisms to secure the APIs using Spring Security.     

5. Testing:     
○ Added unit tests to ensure the correctness of CRUD operations and security features.     
○ Used tools like JUnit, Mockito, and Spring Security testing support for testing.      

6. Documentation:     
○ Provided clear documentation on set up and running the Spring Boot application.      
○ Included instructions on user registration, authentication, and accessing secured APIs.      

 ------------------------------------------
Additional Features   
-------------------------------------------  
○ Cron job scheduled daily at 12:01 AM to check Due Date for all the tasks and set Status to "DUE" if the task is due.


 ------------------------------------------
API Endpoints    
-------------------------------------------    

------
User    (Bypassed Authentication  -  Unauthorized)       
------

/user/register - For Adding User - ADMIN     


Body-     
	{
	        "username":"johan14",
	        "email":"johan14@gmail.com",
	        "password":"1234",
      	    	"enabled": true,
	        "authorities": ["ADMIN"]

	}

/user/register - For Adding User - USER     
	
N.B - If no authority is given on registration - given USER access by default     
Body -      

	{
	        "username":"jane13",
	        "email":"jane13@gmail.com",
	        "password":"1234",
      	    	"enabled":true,
	        "authorities": ["USER"]
	}       



 /user/login - For logging into useraccount    
		Throws ApiRequestException on bad credentials login    
 
Body -
	{
	        "username":"austin14",
	        "password":"1234"
	}       


------
Task     	(All task endpoints are authenticated)      
------

---
Create Task  - POST http://localhost:8080/tasks/create    

ADMIN authorised i.e, only ADMIN can create tasks.      

Body -    
{
  "username": "jane13",
  "task": "Interview5",
  "description": "Do Interview",
  "dueDate": "2024-05-26",
  "status":"IN_PROGRESS"
}     


N.B - Due date must be past the current date or throws TaskCreationException.    
Without status in body, it will set status by default as "IN_PROGRESS".    
Also No same user can have the same taskname, however different users can have the same task name.

---
Update Task     - PUT http://localhost:8080/tasks/update     
ADMIN authorised i.e, only ADMIN can update task details.        

 N.B - Since update is based on taskname and not task_id,    
  		DO NOT CHANGE username and task values while updating task.       

Body-    
{
  "username": "jane13",
  "task": "Interview1",
  "description": "Do Interview 1",
  "dueDate": "2024-05-27",
  "status": "EXTENDED"
}        


---
View All Tasks     - GET   http://localhost:8080/tasks/viewAll       

To view all tasks created by all users - ADMIN authorised    
    
curl -i -X GET \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
 'http://localhost:8080/tasks/viewAll'     


---
View All Tasks by specific User - GET - http://localhost:8080/tasks/view/user/{username}      
    
To view the tasks based on the username given - All users are authorised but authentication is checked i,e all users in database can access.        
N.B - The user(authority USER) can view their tasks by their username. If the user is trying to access tasks of any other users, it will throw a TaskRetrievalException.       
Only the ADMIN can access tasks of other users.          

curl -i -X GET \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
 'http://localhost:8080/tasks/view/user/jane13'       
     
---
View Task Details by Taskname     - GET http://localhost:8080/tasks/viewOne/{taskname}        
Get Task  details by taskname - Gives all task details from all users having the same taskname      

curl -i -X GET \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
 'http://localhost:8080/tasks/viewOne/Interview1'         


---
Delete Task by task_Id     - DELETE   http://localhost:8080/tasks/delete/{taskid}    

curl -i -X DELETE \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
 'http://localhost:8080/tasks/delete/13'      



----------------------------------

Curls

----------------------------------
------------------------
User curls         (Not Authenticated/Authorised APIs i.e, anyone can use)
------------------------

Register - ADMIN      
  
 curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'	{
	        "username":"johan14",
	        "email":"johan14@gmail.com",
	        "password":"1234",
      	    	"enabled":true,
	        "authorities": ["ADMIN"]

	}' \
 'http://localhost:8080/user/register'       

Register - USER      

   
curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'	{
	        "username":"jane13",
	        "email":"jane13@gmail.com",
	        "password":"1234",
      	    	"enabled":true,
	        "authorities": ["USER"]
	}' \
 'http://localhost:8080/user/register'       



LogIn       

curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'	{
	        "username":"austin14",
	        "password":"1234"
	}' \
 'http://localhost:8080/user/login'     




---------------------------
Task Curls       (Authorization Header required)
---------------------------      

Create Task      

curl -i -X POST \
   -H "Content-Type:application/json" \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
   -d \
'{
  "username": "jane13",
  "task": "Interview5",
  "description": "Do Interview",
  "dueDate": "2024-05-26"
}' \
 'http://localhost:8080/tasks/create'        


Update Task         

curl -i -X PUT \
   -H "Content-Type:application/json" \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
   -d \
'{
  "username": "jane13",
  "task": "Interview1",
  "description": "Do Interview 1",
  "dueDate": "2024-05-27",
  "status": "DUE"
}' \
 'http://localhost:8080/tasks/update'         



View All Tasks       

curl -i -X GET \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
 'http://localhost:8080/tasks/viewAll'     

View All Tasks By Username       

curl -i -X GET \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
 'http://localhost:8080/tasks/view/user/jane13'      

View Task Details by Taskname     

curl -i -X GET \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
 'http://localhost:8080/tasks/viewOne/Interview1'      


Delete Task by ID    

curl -i -X DELETE \
   -H "Authorization:Basic YXVzdGluMTQ6MTIzNA==" \
 'http://localhost:8080/tasks/delete/13'       












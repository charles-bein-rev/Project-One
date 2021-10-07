# Expense Reimbursement System (JwA)

## Project Description

The Expense Reimbursement System (ERS) will manage the process of reimbursing employees for expenses incurred while on company time. All employees in the company can log in and submit requests for reimbursements and view their past tickets and pending requests. Managers can log in and view all reimbursement requests and past history for all employees in the company. Managers are authorized to approve and deny requests for expense reimbursements.

## Technologies Used

* Java
* Javalin
* Hibernate
* MariaDB
* HTML, CSS, and JavaScript

## Features

* Login protection for administrators and standard users
* A fast and functional front and back end
* Fully persistent data

## Getting Started
* This project requries a populated MariaDB relational database (I used a database hosted on Amazon Web Services RDS), containing tables:
  * users
    * user_id INT AUTO_INCREMENT KEY
    * username VARCHAR(200) UNIQUE
    * password VARCHAR(200)
    * role ENUM('associate', 'admin')
  * Request
    * request_id INT AUTO_INCREMENT KEY
    * short_description VARCHAR(200) NOT NULL
    * amount FLOAT(10,2) NOT NULL
    * long_description VARCHAR(200)
    * status ENUM('submitted', 'accepted', 'rejected')
    * requester_id INT NOT NULL
    * CONSTRAINT users_fk FOREIGN KEY (requester_id) REFERENCES users (user_id)
* This project also expects the url, username, and password for said database to be stored as system environments, under the names:
  * db_url
  * db_username
  * db_password
> git clone https://github.com/charles-bein-rev/Project-One.git

 Build and run source code in your environment of choice (I used IntelliJ IDEA Ultimate)

## Usage

 Access the system from:  
  > http://localhost:8000/pages/dashboard.html  
  
 Login to site using populated user  
 Navigate as normal  
 
 ## License
 
 This project uses the following license: [MIT License](LICENSE)

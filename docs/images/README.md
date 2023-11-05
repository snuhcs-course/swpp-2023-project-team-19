### Resources for Updating Docs

#### DB Diagrams

DB Diagram can updated via dbdiagram.io. The following is the current DB Diagram for the project.

```
//// Docs: https://dbml.dbdiagram.io/docs
//// -- LEVEL 1
//// -- Schemas, Tables and References
// Creating tables
// You can define the tables with full schema names
//// Docs: https://dbml.dbdiagram.io/docs
// User Table
Table User as U {
  user_id int [pk, increment]
  name varchar 
  password varchar
  created_at datetime
  email varchar  
  avatar varchar 
}

// Event Table
Table Event {
  event_id int [pk, increment]
  host_id int [ref: > U.user_id]
  event_type varchar 
  event_title varchar 
  event_num_participants int
  event_date date [null]
  event_time time [null]
  event_duration varchar 
  event_language varchar 
  event_price int [default: 0]
  event_location varchar 
  event_description text [null]
  created_at datetime [default: `auto_now_add=True`]
  event_num_joined int [default: 0]
}


Table Application {
  application_id int [pk, increment]
  event_id int [ref: > Event.event_id]
  host_id int [ref: > Event.host_id]
  applicant_id int [ref: > U.user_id]
  applicant_name varchar [ref: > U.name]
  applicant_avatar varchar [ref: > U.avatar]
  applicant_contact varchar 
  message varchar
  request_status int
  created_at datetime [default: `auto_now_add=True`]
  updated_at datetime [default: `auto_now_add=True`]
}
```
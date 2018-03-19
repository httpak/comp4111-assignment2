#Library Management System

## Framework

The following frameworks are used.

- Java Spring
- MongoDB

## Prerequisites

The project relies on several build tools and frameworks.

- You are assumed to have Gradle installed for Java dependencies.
- You are assumed to have MongoDB installed for database hosting. You may use this [link](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/) for installation on Ubuntu.

### Build

You may build the project from source with the ```build.gradle``` file included.

### Boot Sequence

You must follow the start up sequence precisely.

- Start the MongoDB service at port ```27017``` with ```sudo service mongod start```.
- Run ```Application.java```.

## API

The following operations are supported. Please note that it is case sensitive.

### Adding Books

You may add books with the following POST request.

```
POST http://localhost:8080/BookManagementService/books
```

The request body shall be a JSON string with the following keys.

key | type | required
--- | --- | ---
Title | String | Yes
Author | String | Yes
Publisher | String | Yes
Year | Number | Yes

### Book Lookup

You may lookup books with the following GET request.

```
http://localhost:8080/BookManagementService/books?
```

The request parameters shall be any combination of the following.

key | type | remarks | required
--- | --- | --- | ---
id | String | a valid hexadecimal representation of an ObjectId | 
title | String | search by title substring | 
author | String | search by author substring | 
year | Number | search by year | 
limit | Number | limit the number of returned results | 
sortby | String | the sorting attribute | required with ```order```
order | String | either ```asc``` or ```desc``` | required with ```sortby```

### Book Loaning and Returning

You may loan books with the following PUT request.

```
PUT http://localhost:8080/BookManagementService/books
```

The request body shall be a JSON String with the following key.

key | type | required
--- | --- | ---
Available | Boolean | Yes

### Book Deletion

You may delete books with the following DELETE request.

```
DELETE http://localhost:8080/BookManagementService/books/{_id}
```

```_id``` shall be the book ID.

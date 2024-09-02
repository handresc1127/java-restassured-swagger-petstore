# Test Cases for Swagger Pet Store API

## Test Case 1: Find Pet by ID
**Purpose:** Verify that a pet can be retrieved by its ID.
### Test Steps:
1. **Send a GET request** to `/api/v3/pet/{id}` with a valid pet ID.
2. **Verify** that the response status code is `200`.
3. **Validate** that the response body contains the correct pet details for the given ID.
### Expected Results:
- **Status Code:** 200
- **Response Body:** Contains details of the pet corresponding to the provided ID.
### Test Data:
- **Valid Pet ID:** 1, 2, 3
---

## Test Case 2: Find Pets by Status
**Purpose:** Verify that pets can be retrieved by their status.
### Test Steps:
1. **Send a GET request** to `/api/v3/pet/findByStatus` with `status=available` as a query parameter.
2. **Verify** that the response status code is `200`.
3. **Validate** that all returned pets have the status "available".
### Expected Results:
- **Status Code:** 200
- **Response Body:** All pets should have the status "available".
### Test Data:
- **Status:** "available"
---

## Test Case 3: Add a New Pet
**Purpose:** Verify that a new pet can be added to the store.
### Test Steps:
1. **Send a POST request** to `/api/v3/pet` with a request body containing new pet details.
2. **Verify** that the response status code is `200`.
3. **Validate** that the response body matches the details of the added pet.
### Expected Results:
- **Status Code:** 200
- **Response Body:** Contains the details of the newly added pet.
### Test Data:
- **New Pet JSON:**
---

## Test Case 4: Update an Existing Pet
**Purpose:** Verify that an existing pet's details can be updated.
### Test Steps:
1. **Send a POST request** to /api/v3/pet with pet details to create it (if not already created).
2. **Send a PUT request** to /api/v3/pet with updated pet details.
3. Verify that the response status code is 200.
4. **Send a GET request** to /api/v3/pet/{id} to retrieve the updated pet details.
5. Validate that the updated pet details are reflected in the response.
### Expected Results:
- PUT Status Code: 200
- GET Response Body: Contains updated details of the pet.
### Test Data:
- **Update Pet JSON:**
---

## Test Case 5: Delete a Pet
**Purpose:** Verify that a pet can be deleted from the store.
### Test Steps:
1. Send a POST request to /api/v3/pet with pet details to create it (if not already created).
2. Send a DELETE request to /api/v3/pet/{id}.
3. Verify that the response status code is 200.
4. Send a GET request to /api/v3/pet/{id} to verify that the pet has been deleted.
### Expected Results:
DELETE Status Code: 200
GET Status Code: 404 (indicating the pet no longer exists)
### Test Data:
- Pet ID: 12
- **Detele Pet JSON:**
---








# Swagger Petstore Sample

## Overview
This is the pet store sample hosted at https://petstore3.swagger.io. For other versions, check the branches.
We welcome suggestion both the code and the API design.
To make changes to the design itself, take a look at https://github.com/swagger-api/swagger-petstore/blob/master/src/main/resources/openapi.yaml.

This is a java project to build a stand-alone server which implements the OpenAPI 3 Spec.  You can find out
more about both the spec and the framework at http://swagger.io.

This sample is based on [swagger-inflector](https://github.com/swagger-api/swagger-inflector), and provides an example of swagger / OpenAPI 3 petstore.

### To run (with Maven)
To run the server, run this task:

```
mvn package jetty:run
```

This will start Jetty embedded on port 8080.

### To run (via Docker)

Expose port 8080 from the image and access petstore via the exposed port. You can then add and delete pets as you see fit.


*Example*:

```
docker build -t swaggerapi/petstore3:unstable .
```

```
docker pull swaggerapi/petstore3:unstable
docker run  --name swaggerapi-petstore3 -d -p 8080:8080 swaggerapi/petstore3:unstable
```


### Testing the server
Once started, you can navigate to http://localhost:8080/api/v3/openapi.json to view the Swagger Resource Listing.
This tells you that the server is up and ready to demonstrate Swagger.

### Using the UI
There is an HTML5-based API tool bundled in this sample--you can view it it at [http://localhost:8080](http://localhost:8080). This lets you inspect the API using an interactive UI.  You can access the source of this code from [here](https://github.com/swagger-api/swagger-ui)

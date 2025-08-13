Hi Junie, could you please inspect the OpenAPI specification in `/openapi/openapi/openapi.yaml`;
This is the API documentation for this project;
Please observe how file references are used;
Note how the file names from path operations are determined from the path of the API,
and how components such as headers and schemas are defined in file references.
Then, also update the file `.junie/guidelines.md` with a description of the API documentation,
file naming conventions, and how components are defined;
Provide instructions about testing the OpenAPI Specification using the command npm test.
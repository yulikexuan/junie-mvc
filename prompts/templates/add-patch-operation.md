# Prompt Variables
Apply the following variables to placeholders in the prompt 
  - Placeholders are denoted by `${variable}` syntax 

## Placeholders Definitions 
The following key value pairs are used to replace placeholders in the prompt; 
Format variable defines the variable name, and value defines the value to replace the placeholder with; 
Defined as `variable name` = `value` pairs in the following list:
  - controller_name = `FooController`

# Task Description
Your task is to add a patch operation to the Spring MVC controller `${controller_name}`.

To complete this task, complete the following steps:
  - Create a new Patch Operation DTO using the naming convention <EntityName>PatchDto. 
  - The new patch DTO will not have any NotNull or NotBlank Constraints
  - Add an update operation to the Mapstruct mapper which ignores null values using
  `@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)` on the update method
  of the mapper.
  - Create a new service method which will update an existing entity using the values of the Patch DTO.
  - Add additional mapper tests, service tests, and MockMVC tests to test the patch operation.
  - Update the OpenAPI documentation for the new operation and DTO.
  - Verify all tests are passing.
  - Verify the OpenAPI specification is valid.
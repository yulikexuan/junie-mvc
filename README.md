# Vibe Programming with Junie 

- [IntelliJ IDEA Junie Playbook](https://www.jetbrains.com/guide/ai/article/junie/intellij-idea/)

## Junie Workflows 

### 1. Set up Guidelines 

- [JetBrains Spring Boot Guidelines with Explanation](https://github.com/JetBrains/junie-guidelines/blob/main/guidelines/java/spring-boot/guidelines-with-explanations.md)

### 2. Generate `requirements.md`

- Create `/prompts/add-dots/requirements-draft.md`
  - ``` 
    Update the BeerController to use DTOs.
    In the package model, create a new POJO called BeerDto with the same properties as the JPA Entity Beer.
    The DTO should use annotations from Project Lombok, including Builder.
    Create a Mapstruct mapper to convert to and from the DTO.
    Mappers should be added to the package mappers.
    When converting from a DTO to the JPA entity ignore the properties for id, createDate and updateDate.
    Convert the service layer to accept DTO objects and to use the Mapstruct mapper for type conversions.
    Update the controller methods to use the new DTO pojo for input and access to service methods.
    ```
- Create a new file `prompts.md` 
  - ```
    1. Analyze the file `/prompts/add_dtos/requirements-draft.md` 
    2. Inspect the project 
    3. Improve and rewrite the draft requirements to a new file called `/prompts/add-dtos/requirements.md` 
    ``` 
  - Submit the content of file `prompts.md` to `Junie` to generate the new file `requirements.md`

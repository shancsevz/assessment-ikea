# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
It is a very solid project, and overall it appears well-suited for implementation using an event-based architecture to improve reliability 
and ensure idempotency. Since we follow a hexagonal architecture, the codebase can be refactored relatively easily to replace direct 
calls to the legacy system with event-driven interactions. Leveraging Hibernate Reactive with Panache would also enable non-blocking 
communication with relational databases, which aligns well with an event-driven architecture. 
Furthermore, adopting events would allow us to implement robust retry mechanisms, ensuring that changes are eventually propagated to the 
legacy system even in cases of temporary connectivity or processing issues.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
Adopting a test-driven approach is far more effective than coding directly, as it helps establish a clear contract for the API and ensures
 that development aligns closely with the defined specifications. This approach naturally promotes a strong separation of concerns between 
 the API definition and its implementation, which contributes to better maintainability and scalability over time. Additionally, by defining 
 and sharing the API specification early—before implementation begins—we enable external systems and UI teams to start their development
  using mock objects, allowing parallel progress and reducing integration delays later in the development cycle.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
We should begin with a TDD approach, ensuring we have a strong understanding of the business functionality,
which will make it easier to quickly identify and write the key test cases. Our initial focus will be on creating test cases for 
critical business logic, ensuring that both positive and negative scenarios are thoroughly covered. Given the advancements in the AI era, 
it would also be beneficial to leverage AI tools to generate test cases for technical scenarios such as memory leaks, performance, and security.
 Additionally, we should implement guardrails in GitHub Actions to enforce code coverage thresholds during pull requests and immediately 
 notify developers if the required coverage levels are not met.
```
----
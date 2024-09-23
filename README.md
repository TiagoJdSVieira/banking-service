Run normally or using Docker: docker-compose up --build

Can be tested using swagger: http://localhost:8080/swagger-ui/index.html

This is the working version, but tests are still ongoing.

Implemented the CQRS principle, separating command and query concerns. 
Implemented a component-based architecture, but we can take it a step further by implementing DDD principles and decoupling the infrastructure and web layers from the "bounded context," which is the component.

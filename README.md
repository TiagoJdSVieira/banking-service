Run normally or using Docker: docker-compose up --build

This is the working version, but tests are still ongoing.

Implemented the CQRS principle, separating command and query concerns. 
Implemented a component-based architecture, but we can take it a step further by implementing DDD principles and decoupling the infrastructure and web layers from the "bounded context," which is the component.

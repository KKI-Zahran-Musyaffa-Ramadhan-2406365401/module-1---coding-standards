# Zahran Musyaffa Ramadhan Mulya
# 2406365401

## 1. Principles Applied to the Project

### Single Responsibility Principle (SRP)
I separated the `CarController` from the `ProductController.java` file and placed it in its own `CarController.java` file. 

### Open-Closed Principle (OCP)
In `CarController`, I changed the dependency from the concrete `CarServiceImpl` to the `CarService` interface.
- **Implementation**: By depending on the `CarService` interface, we can introduce new car-related service implementations without modifying the `CarController` code. 

### Liskov Substitution Principle (LSP)
Removing the inheritance relationship where `CarController` extended `ProductController`.

### Interface Segregation Principle (ISP)
The interfaces in this project, such as `ProductService` and `CarService`, are already small and focused on specific functionalities (CRUD operations).
- **Implementation**: Clients of `CarService` only need to know about car-related operations. We avoid creating a "fat" interface that combines both product and car operations, which would force clients to depend on methods they don't use.

### Dependency Inversion Principle (DIP)
I ensured that `CarController` depends on the `CarService` interface rather than the `CarServiceImpl` implementation.
Car controller and Car service both depend on the abstraction (`CarService`). This makes the system more flexible and easier to test using mocks.

## 2. Advantages of Applying SOLID Principles

1.  **Easier to Maintain**: By following SRP, when a bug occurs in car creation, we know exactly where to look (`CarController` or `CarService`). 
2.  **Increased Flexibility**: If I want to change how cars are stored I just need tocreate a new service implementation without changing the controller.
3.  **Easy to Read**: Smaller and more focused classes and interfaces much easier for developers to read and understand the code.




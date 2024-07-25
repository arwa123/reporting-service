# reporting-service
This application processes the input data and generates various reports.

## Build and Run

### Prerequisites

- Java
- Maven

### Steps

1. Clone the repository:
    ```sh
    git clone https://github.com/arwa123/reporting-service.git
    cd report-generator
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn exec:java -Dexec.mainClass="Main"
    ```

### Running Tests

To run the tests, use:
```sh
mvn test
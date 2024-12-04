## **API Bot Tests**

**This project contains tests for the **TakeProfit API**, which focuses on creating trading bots for cryptocurrency exchanges. The project includes testing various API methods used in the process of creating, modifying, and deleting trading bots.**

## Project Overview

The project consists of two main parts:

1. **API tests using Java and TestNG** for the TakeProfit application.
2. **Postman collections** for testing the same APIs using Postman.

## Technologies

- **Java** – main programming language for writing tests.
- **Gradle** – build automation tool.
- **TestNG** – testing framework.
- **Rest-Assured** – library for REST API testing.
- **HttpClient** – library for making HTTP requests.
- **Logback** – for logging.
- **Dotenv** – for loading environment variables from a `.env` file.
- **Lombok** – to simplify Java code.
- **Gson** – for parsing JSON data.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your_repository.git
   cd your_repository
**Ensure that you have JDK 8 or higher and Gradle installed**
Example dependencies in build.gradle:

dependencies {
implementation 'org.testng:testng:7.10.2'
implementation 'io.rest-assured:rest-assured:5.5.0'
implementation 'org.apache.httpcomponents:httpclient:4.5.14'
implementation 'io.github.cdimascio:dotenv-java:3.0.0'  // For loading environment variables
implementation 'org.slf4j:slf4j-api:1.7.32'
implementation 'ch.qos.logback:logback-classic:1.5.12'  // For logging
}

**Environment Variables**
For secure API key management, the .env file is used to store sensitive information. It is loaded via the dotenv-java library.

**Steps to Set Up:**
Create a .env file in the project root directory and add your API keys:

API_KEY_EXCHANGE=your_api_key
SECRET_EXCHANGE=your_secret_key

**Use the environment variables in your code:**

private static final Dotenv dotenv = Dotenv.configure()
.filename(".env")
.load();
public static final String apiKeyExchange = dotenv.get("API_KEY_EXCHANGE");
public static final String secretExchange = dotenv.get("SECRET_EXCHANGE");


## Running Tests

Locally with IntelliJ IDEA:

**Open the project in IntelliJ IDEA**

Ensure the **.env** file is located in the project root directory.
Run the tests using TestNG.

**With Jenkins:**

Connect your project to Jenkins.

**Configure Jenkins to pass environment variables:**

Use GitHub Secrets to store keys and pass them via environment variables.
Set up Jenkins to automatically run tests using Gradle:
./gradlew test

**Logging**
Logs are handled using Logback and can be used for tracking API request and response data.



## Project Structure
 ![01-12-24.jpg](images%2F01-12-24.jpg)


**ApplicationManager Class**
The ApplicationManager class in the src/main/java/botsystem/core/ directory contains the main API interaction tools. It is responsible for managing API requests, authentication, and providing essential methods for interacting with the Take Profit API.

**Project Status**
This project is currently in development and will be updated with new tests and features over time. We plan to expand the test coverage and improve the overall robustness of the API testing.

**Contributing**
Feel free to fork this repository and submit pull requests. Please make sure your code follows the style guidelines and passes all tests before submitting.

**License**
This project is licensed under the MIT License - see the LICENSE file for details.****
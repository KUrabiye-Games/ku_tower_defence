# KU Tower Defence

A tower defence game project using JavaFX.

## Prerequisites

- Java Development Kit (JDK) 24
  - Note: The project is configured to automatically download JDK 24 if it's not available on your system

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Clone the Repository

```bash
git clone https://github.com/YourUsername/ku_tower_defence.git
cd ku_tower_defence
```

### Building the Project

This project uses Gradle Wrapper, which means you don't need to install Gradle to build and run the project. However, you need at least one java installation on your device to run auto-downloader.

To build the project:

#### On Windows:

```bash
gradlew.bat build
```

#### On Linux/macOS:

```bash
./gradlew build
```

### Running the Application

To run the application:

#### On Windows:

```bash
gradlew.bat run
```

#### On Linux/macOS:

```bash
./gradlew run
```

### Running Tests

To run the tests:

#### On Windows:

```bash
gradlew.bat test
```

#### On Linux/macOS:

```bash
./gradlew test
```

## Project Structure

```
ku_tower_defence/
├── app/
│   ├── build.gradle.kts      # Application build configuration
│   └── src/
│       ├── main/java/        # Application source code
│       │   └── com/
│       │       └── kurabiye/
│       │           └── kutd/
│       │               └── app/
│       │                   └── App.java  # Main application class
│       └── test/java/        # Test source code
│           └── com/
│               └── kurabiye/
│                   └── kutd/
│                       └── app/
│                           └── AppTest.java
├── gradle/
│   ├── libs.versions.toml    # Dependency version management
│   └── wrapper/              # Gradle wrapper files
├── gradlew                   # Gradle wrapper script for Linux/macOS
├── gradlew.bat               # Gradle wrapper script for Windows
└── settings.gradle.kts       # Gradle settings
```

## Built With

- [Gradle](https://gradle.org/) - Dependency Management and Build Tool
- [JavaFX](https://openjfx.io/) - GUI Framework for Java
- [JUnit Jupiter](https://junit.org/junit5/) - Testing Framework

## License

This project is licensed under nothing. Reach out to apolat21@ku.edu.tr for any quest.
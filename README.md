# Spring Security MFA Demo

This project demonstrates how to implement **Multi-Factor Authentication (MFA)** using the latest features in **Spring Security 7** (with Spring Boot 4.x and Java 25).

It showcases a two-step authentication flow:
1. **First Factor**: Standard Username and Password.
2. **Second Factor**: One-Time Token (OTT) delivered via a "magic link" (simulated in logs).

## 🚀 Features

- **Spring Security 7 MFA**: Uses the new `@EnableMultiFactorAuthentication` annotation to enforce multiple authentication factors.
- **One-Time Token (OTT)**: Implementation of the One-Time Token login flow.
- **Custom PIN Service**: A custom `OneTimeTokenService` that generates 6-digit random PINs.
- **Magic Link Handler**: A success handler that generates a login URL with the token and prints it to the console (simulating an email/SMS delivery).
- **Role-Based Access Control**: Different access levels for `/` (public), `/admin/**` (ADMIN only), and other authenticated routes.

## 🛠 Prerequisites

- **Java 25** or higher (required for this experimental version).
- **Maven 3.8+**.

## 🏗 Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/coderjingtao/spring-security-7-mfa-demo.git
   cd spring-security-mfa-demo
   ```

2. **Build the project**:
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

## 🧪 Testing the MFA Flow

### 1. Initial Login
Navigate to `http://localhost:8080/`. You will be redirected to the default login page.
Use one of the following credentials:

| Username | Password | Roles |
| :--- | :--- | :--- |
| `user` | `password` | `USER` |
| `admin` | `password` | `ADMIN`, `USER` |

### 2. OTT Generation
After entering your password, Spring Security will detect that a second factor is required.
1. Check the **application logs** (console output).
2. Look for a line starting with `magicLink: http://localhost:8080/login/ott?token=XXXXXX`.
3. Copy and paste that link into your browser.

### 3. Authentication Complete
Once the OTT is consumed, you will be granted the full authorities required to access the application.

## 📂 Project Structure

- `SecurityConfig.java`: Configures the MFA flow, security filters, and in-memory users.
- `PinOneTimeTokenService.java`: Custom logic for generating and consuming 6-digit PIN OTTs.
- `OneTimeTokenSuccessHandler.java`: Generates the magic link and redirects the user after token generation.
- `HomeController.java`: Simple endpoints to verify authentication state.

## 📝 License

This project is open-source and available under the MIT License.

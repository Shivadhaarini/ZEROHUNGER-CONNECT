# ZeroHunger Connect — Web Application

A full-stack Java web app for the ZeroHunger Connect food donation system —
SDG 2: Zero Hunger. This is the browser-based evolution of the original
console demo: same domain model (Donor, NGO, Volunteer, Donation), now with
a real login system, database, and role-based dashboards instead of
`System.out.println`.

## Stack

| Layer        | Technology                          |
|--------------|--------------------------------------|
| Language     | Java 17                              |
| Framework    | Spring Boot 3.3 (Web MVC)            |
| Persistence  | Spring Data JPA + Hibernate          |
| Database     | MySQL                                |
| Security     | Spring Security (BCrypt + roles)     |
| Frontend     | Thymeleaf (server-rendered HTML) + custom CSS |

## Project structure

```
src/main/java/com/zerohunger/backend/
  entity/         JPA entities: AppUser, Donor, NgoOrg, VolunteerProfile,
                  FoodDonation, FoodItem, LocationCoordinates, Distribution
  repository/     Spring Data JPA repositories
  service/        Business logic (RegistrationService, FoodDonationService)
  controller/     MVC controllers (one per module: Donor/NGO/Volunteer/Admin)
  config/         Spring Security config + default admin seeder
src/main/resources/
  templates/      Thymeleaf HTML pages (login, register, 4 dashboards)
  static/css/     style.css — full custom design system
  application.properties   MySQL connection + app settings
```

## Setup

### 1. Prerequisites
- Java 17+ (JDK)
- Maven (or use IntelliJ's bundled Maven)
- MySQL Server running locally

### 2. Create the database
The app can auto-create the database on first connection
(`createDatabaseIfNotExist=true` is already set), but you still need MySQL
itself running. Optionally create it yourself:

```sql
CREATE DATABASE zerohunger_connect;
```

### 3. Configure your credentials
Open `src/main/resources/application.properties` and set your MySQL
username/password:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 4. Run it

**From IntelliJ:** Open the project folder, let Maven import the
dependencies, then run `ZeroHungerConnectApplication.java`.

**From the command line:**
```bash
mvn spring-boot:run
```

The app starts on **http://localhost:8080**

### 5. Log in

On first run, a default admin account is created automatically:

- **Username:** `admin`
- **Password:** `admin123`

(Change this in `application.properties` under `zerohunger.admin.*` before
your first run if you want different credentials, or change the password
after logging in.)

Everyone else — Donors, NGOs, Volunteers — self-registers at `/register`.
New NGO accounts start **unverified**; log in as admin and verify them
under the Admin dashboard before they can accept donations.

## Walkthrough (suggested demo flow)

1. Register a **Donor** account → submit a donation with 1–2 food items.
2. Register an **NGO** account → log in as **admin**, verify the NGO.
3. Log back in as the NGO → accept the pending donation.
4. Register a **Volunteer** account.
5. As the NGO, assign that volunteer to the accepted donation.
6. Log in as the volunteer → confirm delivery.
7. Log in as admin → see the donation move to "Delivered" and the
   "Kg Food Rescued" stat update.

## Notes on what changed from the console version

- The abstract `User` superclass became `AppUser` (login identity) +
  composition instead of inheritance, since JPA table-per-class
  inheritance adds complexity without benefit here.
- `FoodItem` was a Java 21 `record` in the console app; here it's a plain
  `@Embeddable` class (JPA embeddables don't map cleanly onto records
  across Hibernate versions), keeping the same three fields.
- All the original business logic — `verifyDonation()`, `getTotalQuantity()`,
  status tracking — is preserved on the entities/services, just returning
  data to controllers instead of printing to console.
- The Streams-API statistics demo (total food rescued, donations grouped
  by status, verified NGOs) is now the live Admin dashboard.

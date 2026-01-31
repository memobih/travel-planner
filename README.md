# Travel Destination Planner

Full-stack application using Spring Boot (backend) , Angular  (front end) and MYSQL (data base)
## Requirements
- Java 17+
- Maven
- Node.js 18+
- Angular CLI 16+
- npm or yarn
- Git
---
## Backend (Spring Boot)
### 1. Navigate to backend folder
cd travel_destination

### 2. Build the project
mvn clean install

### 3. Run the project
mvn spring-boot:run

The backend will start at:
http://localhost:8081
### 4. API Endpoints
**Auth**
- POST /api/auth/register - Register user/admin
- POST /api/auth/login - Login (returns JWT)
**Admin**
- POST /api/admin/bulk - Bulk create destinations
- DELETE /api/admin/bulk - Bulk delete destinations
- GET /api/admin - Get paginated destinations (with keyword search)
**User**
- GET /api/user - Get all destinations (paginated + keyword search)
- POST /api/user/want-to-visit - Mark destinations as want-to-visit
- POST /api/user/unwant-to-visit - Unmark destinations
- GET /api/user/want-to-visit - Get all want-to-visit destinations (paginated + keyword search)
---

## Frontend (Angular)

### 1. Navigate to frontend folder
cd front_end

### 2. Install dependencies
npm install

### 3. Run Angular development server
ng serve --open

The frontend will start at:
http://localhost:4200

---


## Running Full Stack Together

1. Start Spring Boot backend first (localhost:8080)
2. Start Angular frontend (localhost:4200)
3. Login as admin/user       (username : admin , passwoed :admin123) (this is for admin only created with seeding)
4. Admin can add, bulk add, delete destinations
5. User can view destinations, search, mark "Want to Visit"

---

## Notes

- Make sure backend is running before using frontend
- Use JWT token returned from login to access protected routes
- Pagination parameters: page (0-based), size (number of items per page)
- Keyword search supported via query param: keyword

---

## Default Roles

- ADMIN → Can manage destinations
- USER → Can view and mark destinations

## app Features

- Login/Register page
- Admin Dashboard
  - Bulk add destination
  - Bulk delete
  - Paginated table with search
- User Dashboard
  - View all destinations (paginated + search)
  - Mark/Unmark "Want to Visit"
  - View all marked destinations

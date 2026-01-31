# Travel Destination Planner

Full-stack application using Spring Boot (backend) and Angular + Tailwind CSS (frontend) for managing travel destinations.

---

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
http://localhost:8080

### 4. API Endpoints

**Auth**
- POST /api/auth/register - Register user/admin
- POST /api/auth/login - Login (returns JWT)

**Admin**
- POST /api/admin - Create destination
- POST /api/admin/bulk - Bulk create destinations
- DELETE /api/admin/bulk - Bulk delete destinations
- GET /api/admin - Get paginated destinations (with keyword search)

**User**
- GET /api/user - Get all destinations (paginated + keyword search)
- POST /api/user/want-to-visit - Mark destinations as want-to-visit
- POST /api/user/unwant-to-visit - Unmark destinations
- GET /api/user/want-to-visit - Get all want-to-visit destinations (paginated + keyword search)

> Note: Use JWT token in Authorization: Bearer <token> header for protected endpoints.

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

## Project Structure (Frontend)

src/
├── app/
│   ├── core/             # Services, interceptors, auth
│   ├── shared/           # Models, utils
│   ├── features/
│   │   ├── auth/         # Login/Register components
│   │   ├── admin/        # Admin dashboard + components
│   │   └── user/         # User dashboard + components
│   └── app.module.ts

- core/services/destination.service.ts handles all API calls to backend
- JWT token stored in localStorage after login
- Tailwind CSS used for UI styling

---

## Running Full Stack Together

1. Start Spring Boot backend first (localhost:8080)
2. Start Angular frontend (localhost:4200)
3. Login as admin/user
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

---

## Example Backend Requests

Login Admin
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Get Paginated Destinations
GET http://localhost:8080/api/user?keyword=paris&page=0&size=10
Authorization: Bearer <JWT_TOKEN>

Mark Destination as Want to Visit
POST http://localhost:8080/api/user/want-to-visit
Authorization: Bearer <JWT_TOKEN>
[1, 2, 3]  # List of destination IDs

---

## Frontend Features

- Login/Register page
- Admin Dashboard
  - Add destination
  - Bulk add
  - Bulk delete
  - Paginated table with search
- User Dashboard
  - View all destinations (paginated + search)
  - Mark/Unmark "Want to Visit"
  - View all marked destinations

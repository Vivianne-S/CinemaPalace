🎬 CinemaPalace

Cinema booking app with Kotlin, Jetpack Compose & Ktor backend – showcasing modern architecture and JWT security.

CinemaPalace is a modern cinema booking app built with Kotlin – featuring a sleek Jetpack Compose UI and a secure Ktor backend. Browse movies and trailers, explore showtimes, select seats in real time, and book tickets with QR codes. The project demonstrates best practices in mobile development, architecture, and security (JWT auth, refresh tokens, REST + WebSockets).

⸻

🚀 Features (MVP)
	•	✅ Secure backend with JWT (RS256, refresh tokens)
	•	✅ Android app with Jetpack Compose UI
	•	✅ Login flow with encrypted token storage
	•	✅ Auth-protected API routes
	•	🔜 Movie listing via TMDB proxy
	•	🔜 Cinemas & showtimes
	•	🔜 Real-time seat selection (WebSockets)
	•	🔜 Reservations & ticket confirmation (QR codes)
	•	🔜 Stripe / Swish payments

⸻

🏗 Tech Stack

Backend
	•	Ktor 3 (Kotlin)
	•	JWT Auth (RS256, refresh tokens)
	•	Exposed ORM + PostgreSQL (planned)
	•	WebSockets for real-time seats
	•	Docker (planned)

Android App
	•	Kotlin, Jetpack Compose
	•	Hilt for DI
	•	Retrofit + OkHttp
	•	EncryptedSharedPreferences for token storage
	•	Room (planned for offline tickets)

⸻

▶️ Getting Started

Backend

cd server
./gradlew run

Server runs at http://localhost:8080

Endpoints:
	•	GET /ping
	•	POST /auth/login
	•	POST /auth/refresh
	•	GET /.well-known/jwks.json
	•	GET /secure/echo (JWT required)

Android
	•	Put API_BASE_URL=http://10.0.2.2:8080/ in local.properties
	•	Run app in emulator
	•	Test flows:
	•	Ping backend → pong=true
	•	Login (user: test@test.com, pass: 1234)
	•	/secure/echo → 200 OK (JWT validated, refresh tested)

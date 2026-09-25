# IWVG DevOps — Liu Ningchang

Spring Boot REST API for the *Ingeniería Web: Visión General (IWVG)* DevOps
course (UPM). This repository is the personal solution of **Liu Ningchang**
based on the official assignment template published at
[`miw-upm/iwvg-devops-template`](https://github.com/miw-upm/iwvg-devops-template).

The application exposes a small user management API, is built and verified
through GitHub Actions + SonarCloud, and is deployed to **AWS EC2** for
staging. The `main` branch workflow publishes Docker images to
**GitHub Container Registry (GHCR)**; the actual AWS production deployment
step on `main` is intentionally disabled.

---

## Build and quality badges

[![CI](https://img.shields.io/github/actions/workflow/status/NingchangLiu-UPM/iwvg-devops-liu-ningchang/continuous-integration.yml?branch=develop&label=CI)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-integration.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=NingchangLiu-UPM_iwvg-devops-liu-ningchang&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=NingchangLiu-UPM_iwvg-devops-liu-ningchang)
[![CD Staging](https://img.shields.io/github/actions/workflow/status/NingchangLiu-UPM/iwvg-devops-liu-ningchang/continuous-deployment.yml?branch=staging&label=CD%20staging)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-deployment.yml)
[![CD Main](https://img.shields.io/github/actions/workflow/status/NingchangLiu-UPM/iwvg-devops-liu-ningchang/cd-main.yml?branch=main&label=CD%20main)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/cd-main.yml)
[![License](https://img.shields.io/github/license/NingchangLiu-UPM/iwvg-devops-liu-ningchang?color=informational)](./LICENSE.md)

> **Note on deployment status**: the existing staging CD workflow uses a
> security-hardened configuration (see Issue #16). This updated workflow has
> been merged into `develop` and validated by CI, but **it has not yet been
> pushed to `staging` and has not been executed on staging** at the time of
> this README. The existing staging application is still running an image
> that was deployed **before** the Issue #16 workflow changes.

---

## Technology stack

- **Language / build**: Java 21, Maven (Spring Boot 3.5.5 parent).
- **Framework**: Spring Boot (Web, Security, Data JPA, Actuator).
- **Persistence**: PostgreSQL (production, `dev`, `prod` profiles) and
  H2 in-memory (test profile).
- **API documentation**: springdoc OpenAPI (`/swagger-ui.html`,
  `/v3/api-docs`).
- **Container**: Docker, Docker Compose; multi-stage `Dockerfile`.
- **Quality**: JUnit 5, AssertJ, Mockito (Surefire + Failsafe),
  JaCoCo coverage report, SonarCloud analysis.
- **CI/CD**: GitHub Actions (CI on `develop`/`staging`, staging CD on
  `staging`, main CD on `main`).
- **Cloud**: AWS EC2 + Amazon ECR (staging), GitHub Container Registry
  (`ghcr.io`, main).

---

## Project architecture

Layered Spring Boot application:

```
es.upm.miw.devops
├── Application                     # Spring Boot bootstrap
├── SecurityConfiguration           # Stateless SecurityFilterChain
└── rest
    ├── SystemResource              # /, /version-badge
    ├── exceptionshandler           # ApiExceptionHandler, ErrorMessage
    └── user                        # User domain
        ├── User                    # JPA entity
        ├── UserDto / UserActivePatchDto / UserFindCriteria
        ├── UserRepository          # Spring Data JPA
        ├── UserService             # @Transactional business logic
        ├── UserResource            # /users endpoints
        ├── Role / Province         # enums
        └── SeederForDev            # dev/test fixture data
```

Profiles:

- `application.yml` — defaults, defaults to `dev`.
- `application-dev.yml` — local PostgreSQL on `localhost:5432`.
- `application-prod.yml` — env-driven datasource (`SPRING_DATASOURCE_*`),
  used by the staging CD workflow.
- `application-test.yml` — H2 in-memory database.

---

## Implemented API

All endpoints are mounted under `/users` (defined in `UserResource`).
The system endpoints (`SystemResource`) live at the root.

### `/users` — user management

| Method | Path                  | Purpose                                           |
|--------|-----------------------|---------------------------------------------------|
| GET    | `/users`              | Search users (`?active=`, `?mobile=`, `?billable=`). |
| GET    | `/users/{id}`         | Read a single user.                                |
| PUT    | `/users/{id}`         | Replace user fields (path id is authoritative).    |
| PUT    | `/users/{id}/active`  | Set the `active` flag of one user.                 |
| PATCH  | `/users`              | Batch update of `active` (ADMIN deactivation rejected with `409 Conflict`). |
| DELETE | `/users/{id}`         | Remove a user.                                     |

### `/` and system endpoints

| Method | Path              | Purpose                                   |
|--------|-------------------|-------------------------------------------|
| GET    | `/`               | Build info + endpoint catalog (HTML).     |
| GET    | `/version-badge`  | SVG badge with current version (read by `SystemResource.VERSION_BADGE`). |
| GET    | `/swagger-ui.html`| OpenAPI UI.                                |
| GET    | `/v3/api-docs`    | OpenAPI JSON.                              |
| GET    | `/actuator/health`| Application health endpoint (used by the staging CD health check). |
| GET    | `/actuator/info`  | Build metadata.                            |

---

## Requirements and local setup

- JDK 21.
- Maven 3.9.x (or use the included wrapper if present).
- Docker + Docker Compose (optional, for the containerised run).

Clone:

```sh
git clone https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang.git
cd iwvg-devops-liu-ningchang
```

Local PostgreSQL (matches `application-dev.yml`) or H2 (test profile).
For the local Docker Compose run, create the external network once:

```sh
docker network create devopsNet
```

---

## Running with Maven

Run unit + integration + functional tests and build the JAR:

```sh
mvn -B verify
```

Run the application locally against the `dev` profile
(uses `application-dev.yml`, expects a local PostgreSQL with database
`devopsDb`, user `postgres`, password `postgres`):

```sh
mvn spring-boot:run
```

Then browse to `http://localhost:8080/swagger-ui.html`.

---

## Running with IntelliJ

1. **Open** the cloned folder in IntelliJ IDEA (the project will be
   detected as a Maven project).
2. Run the `Application` class (right-click → *Run*).
3. The `dev` profile is active by default.

---

## Running with Docker / Docker Compose

Build the image (multi-stage `Dockerfile`, final stage is
`eclipse-temurin:21-jre-alpine`, JAR launched with `java -jar app.jar`):

```sh
docker build -t devops:latest .
```

Start the API container on the pre-existing `devopsNet` network (port
`8080` published to the host):

```sh
docker run -d --name devops1 -p 8080:8080 --network devopsNet devops:latest
```

Or use the included `docker-compose.yml`:

```sh
docker compose up --build -d
```

For the local database stack, a separate `docker-compose-db.yml` is
provided (PostgreSQL plus optional MySQL and MongoDB containers). It
targets the same `devopsNet` network:

```sh
docker compose -f docker-compose-db.yml -p databases up -d
```

The `docker-compose.yml` image is named `devops:latest` (local name)
and is independent from the `ECR_REGISTRY/ECR_REPOSITORY` naming used
by the staging CD workflow.

---

## Testing and quality analysis

- **Unit, integration and functional tests** are wired via
  `maven-surefire-plugin` + `maven-failsafe-plugin` (`*IT.java`,
  `*FT.java` conventions; functional tests use `WebTestClient`).
- **Coverage** is collected with `jacoco-maven-plugin` during the
  `verify` phase and reported via SonarCloud.
- **SonarCloud** project: `NingchangLiu-UPM_iwvg-devops-liu-ningchang`
  on organisation `ningchangliu-upm-1`. The Sonar Maven plugin is
  declared in `pom.xml`; the analysis is invoked from
  `continuous-integration.yml` only on pushes to `develop`.

---

## CI/CD workflows

The workflows live under `.github/workflows/`.

### `continuous-integration.yml` — CI

- Triggers: pushes to `develop` and `staging`.
- Java 21 (Temurin), full `mvn -B verify`.
- CodeQL analysis (`security-extended` queries).
- SonarCloud analysis **only on `develop`** (gated by
  `github.ref == 'refs/heads/develop'`).
- GitHub Actions SHAs and CodeQL SHAs are pinned.

### `continuous-deployment.yml` — staging CD

- Triggers: pushes to `staging`.
- Builds and pushes the Docker image to **Amazon ECR**
  (`ECR_REGISTRY=727660953632.dkr.ecr.eu-south-2.amazonaws.com`,
  `ECR_REPOSITORY=iwvg-devops-liu-ningchang`, tags `latest` and
  `${{ github.sha }}`).
- Connects to the staging **EC2** host over SSH
  (`AWS_HOST`, `AWS_USER`, `AWS_SSH_PRIVATE_KEY` as step-level env
  variables — see Issue #16).
- Deploys the API container `iwvg-devops` on network `devopsNet`
  and depends on the existing `postgres-server` PostgreSQL container.
- Runs `/actuator/health` (HTTP 200 + `"status":"UP"`) up to 20 times
  with a 3-second sleep; on failure it prints logs and rolls back to
  the previously captured image (`PREV_IMAGE`).
- Cleans the temporary SSH key on every run (`if: always()`).

### `cd-main.yml` — main CD

- Triggers: pushes to `main`.
- Builds and pushes the Docker image to **GitHub Container Registry**
  (`ghcr.io/<repo>:<version>` and `:latest`).
- The actual AWS production deployment step is **commented out /
  intentionally disabled** in this repository (cost-saving placeholder
  kept for parity with the teacher's current `cd-main.yml`). The
  workflow currently only publishes the GHCR image.

---

## Deployment architecture and status

### Staging environment (current, manually checked)

| Component | Value                                                    |
|-----------|----------------------------------------------------------|
| Cloud     | AWS EC2 (region `eu-south-2`).                            |
| Registry  | Amazon ECR (`iwvg-devops-liu-ningchang`).                 |
| Container | `iwvg-devops` (image `ECR_REGISTRY/.../iwvg-devops-liu-ningchang:latest`). |
| Network   | Docker `devopsNet` (external).                            |
| Database  | PostgreSQL container `postgres-server` on the same network. |
| Health    | `GET /actuator/health` → `HTTP 200 {"status":"UP"}`.      |

The existing staging instance is still running an image that was
deployed before the Issue #16 workflow changes. The Issue #16-hardened
CD workflow has not been executed on the `staging` branch, so this
README does not claim that the updated workflow has been validated
end-to-end against AWS.

### Infrastructure difference vs the assignment template

The official assignment describes an ecosystem based on **AWS Lightsail,
Ubuntu 22.04 LTS and GitHub Packages**. This student solution is
implemented on **AWS EC2 / Amazon ECR** for staging and on
**GitHub Container Registry (`ghcr.io`)** for the `main` branch.
The deployment has not been migrated to Lightsail in this repository.

---

## Releases

Formal release tags currently published in this repository:

| Tag         | Type    | Branch   |
|-------------|---------|----------|
| `6.3.0`     | Release | `main`   |
| `6.3.0-RC1` | RC      | `staging`|
| `6.2.0`     | Release | `main`   |
| `6.2.0-RC1` | RC      | `staging`|
| `6.1.0` … `6.1.0-RC5` | RC / release history | `backup/*` branches |
| `6.0.0`, `6.0.0-RC1`  | Initial release | `master` |

The version declared in `pom.xml` is currently `6.0.0`; it has **not**
been bumped in lock-step with the latest tag (`6.3.0`). This known gap
is documented here explicitly to avoid confusion.

---

## GitHub project and assignment references

- Repository: <https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang>
- GitHub Issues: <https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/issues>
- Official assignment template:
  <https://github.com/miw-upm/iwvg-devops-template>
- SonarCloud project:
  <https://sonarcloud.io/summary/new_code?id=NingchangLiu-UPM_iwvg-devops-liu-ningchang>
- Course (UPM): <https://miw.etsisi.upm.es>

The official assignment itself remains in the teacher's template
repository; this README only describes the student implementation and
must not be confused with the official 4.x Fraction/UsersDatabase
exercises that appear in the legacy README template.

---

## License

Released under the [MIT License](./LICENSE.md), copyright Universidad
Politécnica de Madrid.

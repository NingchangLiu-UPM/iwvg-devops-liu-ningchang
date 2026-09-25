
# IWVG DevOps — Liu Ningchang

Personal assignment repository for Ingeniería Web: Visión General (IWVG), Máster en Ingeniería Web, Universidad Politécnica de Madrid.

This project implements a Spring Boot REST API with GitHub Actions, SonarCloud, Docker, PostgreSQL and AWS deployment.

Official assignment: [miw-upm/iwvg-devops-template](https://github.com/miw-upm/iwvg-devops-template/blob/develop/README.md).

## Project status

[![CI](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-integration.yml/badge.svg?branch=develop)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-
[![CI](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-integration.yml/badge.svg?branch=develop)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-integration.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=NingchangLiu-UPM_iwvg-devops-liu-ningchang&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=NingchangLiu-UPM_iwvg-devops-liu-ningchang)
[![CD Staging](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-deployment.yml/badge.svg?branch=staging)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-deployment.yml)
[![CD Main](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/cd-main.yml/badge.svg?branch=main)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/cd-main.yml)
[![AWS EC2](https://img.shields.io/badge/AWS_EC2-staging_deployed-FF9900?logo=amazonaws&logoColor=white)](http://51.48.72.167:8080/swagger-ui.html)
[![License](https://img.shields.io/github/license/NingchangLiu-UPM/iwvg-devops-liu-ningchang)](./LICENSE.md)

## Branches and releases

- `develop`: ongoing development and Stage 8 quality improvements.
- `staging`: release-candidate branch.
- `main`: formal release branch.
- Latest formal release: `6.3.0`.
- Release history: [GitHub Releases](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/releases).

The Maven version declared in `pom.xml` remains `6.0.0` and has not been synchronized with the latest Git release tag.

## Implemented API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/users` | Search users by active, mobile and billable criteria |
| GET | `/users/{id}` | Read a user |
| PUT | `/users/{id}` | Update a user |
| PUT | `/users/{id}/active` | Update a user's active status |
| PATCH | `/users` | Batch-update active status |
| DELETE | `/users/{id}` | Delete a user |

The batch update prevents deactivation of ADMIN users and preserves transaction rollback on failure.

## Technology stack

Java 21, Maven, Spring Boot, Spring Data JPA, PostgreSQL, H2, JUnit 5, JaCoCo, SonarCloud, CodeQL, GitHub Actions, Docker and OpenAPI.

## Local setup

Clone this repository:

```bash
git clone https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang.git
cd iwvg-devops-liu-ningchang
```

Run the tests and build:

```bash
mvn -B verify
```

For local development, configure the PostgreSQL database and start the Spring Boot application.

API documentation is available at `/swagger-ui.html`.

## CI/CD and deployment

- CI validates the application with Maven and CodeQL.
- SonarCloud analysis runs on pushes to `develop`.
- The staging environment uses AWS EC2 and Amazon ECR.
- The `main` workflow publishes Docker images to GitHub Container Registry (GHCR).
- AWS production deployment from `main` is intentionally disabled.

The staging infrastructure differs from the assignment's specified AWS Lightsail setup. The security-hardened staging workflow developed in Issue #16 has not been executed on the staging branch; the existing application was deployed before those workflow changes.

## Project resources

- [GitHub Project](https://github.com/users/NingchangLiu-UPM/projects/3)
- [Issues](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/issues)
- [Actions](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions)
- [SonarCloud](https://sonarcloud.io/project/overview?id=NingchangLiu-UPM_iwvg-devops-liu-ningchang)
- [Official assignment](https://github.com/miw-upm/iwvg-devops-template/blob/develop/README.md)

## [Master's Degree in Web Engineering at Universidad Politécnica de Madrid (miw-upm)](http://miw.etsisi.upm.es)

## Web Engineering: Overview (IWVG) — DevOps Assignment

> Course assignment by **Ningchang Liu** (Master's in Web Engineering, UPM, 2025–2026).
> This repository is the personal solution to the IWVG DevOps practice. Each release matches a milestone of the assignment.

[![License](https://img.shields.io/github/license/NingchangLiu-UPM/iwvg-devops-liu-ningchang?color=informational)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/blob/master/LICENSE.md)
[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/NingchangLiu-UPM/iwvg-devops-liu-ningchang?include_prereleases&color=informational)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/releases)
![GitHub Release Date](https://img.shields.io/github/release-date/NingchangLiu-UPM/iwvg-devops-liu-ningchang?color=informational)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/NingchangLiu-UPM/iwvg-devops-liu-ningchang)
![GitHub issues](https://img.shields.io/github/issues/NingchangLiu-UPM/iwvg-devops-liu-ningchang?color=important)
![GitHub closed issues](https://img.shields.io/github/issues-closed/NingchangLiu-UPM/iwvg-devops-liu-ningchang?color=informational)

### Code Status

[![CI](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-integration.yml/badge.svg?branch=develop)](https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang/actions/workflows/continuous-integration.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=NingchangLiu-UPM_iwvg-devops-liu-ningchang&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=NingchangLiu-UPM_iwvg-devops-liu-ningchang)
<!-- Badge 3: Render deployment — will be added during Sprint 1, Issue #3 -->

### Required Technologies

`Java 21` `Maven` `GitHub` `GitHub Actions` `SonarCloud` `Spring Boot` `GitHub Packages` `Docker` `Render` `OpenAPI`

### :gear: Project Installation

1. Clone the repository on your machine, **via console**:

```sh
cd <folder path>
git clone https://github.com/NingchangLiu-UPM/iwvg-devops-liu-ningchang
```

2. Import the project with **IntelliJ IDEA**
    * Use **Open**, then select the project folder.

### :gear: Running Locally with IntelliJ

* Run the `Application` class from IntelliJ IDEA.

### :gear: Running Locally with Docker

* Create the network (only once):

```sh
docker network create devopsNet
```

* Verify the network:

```sh
docker network ls
```

* Build the image and start the container ( :warning: **include the final dot** ):

```sh
docker build -t devops:latest .
docker run -d --name devops1 -p 8080:8080 devops
```

* Or use Docker Compose (uses `docker-compose.yml`):

```sh
docker compose up --build -d
```

* Web client: `http://localhost:8080`

### :book: Slides

* [DevOps slides](docs/miw-iwvg-devops-slides.pdf)

### :page_with_curl: IWVG DevOps — Assignment Statement

> All deliverables must be in English.

#### 1. Create the project (**0.5 pt**)

Create a Maven project named **iwvg-devops-liu-ningchang**, version **5.0.0**. A zip template is provided.

> Remember to edit `pom.xml` and change the `artifactId`.
> Remember to rename the folder.
> Import it from IntelliJ.
> Create a repository on GitHub with the first commit message: `"Initial. Ningchang Liu"`.

#### 2. Prepare Scrum management (**0.5 pt**)

Create a management project on GitHub and prepare it for Scrum methodology (columns, labels, milestones…). Make it **public** so it is visible.

#### 3. Sprint 1 — Ecosystem setup (**1.5 pts**)

Three issues will be created, but development will only use `develop` & `master` branches.

* :one: Continuous integration with **GitHub Actions**. Include the **Badge** in README with a **link**.
* :two: Code analysis with **SonarCloud**. Include the **Badge** in README with a **link** to the Sonar project.
* :three: Deployment with **Render**. Include the **Badge** in README with a **link** to the Render service.

> :one:, :two:, :three: indicates the temporal development order.

#### 4. Release (**0.5 pt**)

> First release of the code (***v5.0.0-Release***).

#### 5. Sprint 2 — Software preparation (**2 pts**)

Four issues will be created.

* Classes :one:**Fraction** & :five:**FractionTest**.
* Classes :two:**User** & :three:**UserTest**.
* Class :four:**UsersDatabase**.
* Extension :six:**Fraction** & :seven:**FractionTest** with: **isProper, isImproper, isEquivalent, add, multiply, divide**.

> Each issue must be merged back into `develop` when closed.
> Second release of the code (***v5.1.0-Release***).

#### 6. Sprint 3 — Four searches (**3.5 pts**)

Based on the first four distinct hexadecimal characters of the last commit hash of the previous release, choose four searches from the table and implement them with tests. Each search is one issue.

* `0` `Stream<String> findUserFamilyNameInitialBySomeProperFraction();`
* `1` `Stream<String> findUserIdBySomeProperFraction();`
* `2` `Fraction findFractionMultiplicationByUserFamilyName(String familyName);`
* `3` `Fraction findFractionDivisionByUserId(String id);`
* `4` `Double findFirstDecimalFractionByUserName(String name);`
* `5` `Stream<String> findUserIdByAllProperFraction();`
* `6` `Stream<Double> findDecimalImproperFractionByUserName(String name);`
* `7` `Fraction findFirstProperFractionByUserId(String id);`
* `8` `Stream<String> findUserFamilyNameBySomeImproperFraction();`
* `9` `Fraction findHighestFraction();`
* `a` `Stream<String> findUserNameBySomeImproperFraction();`
* `b` `Stream<String> findUserFamilyNameByAllNegativeSignFractionDistinct();`
* `c` `Stream<Double> findDecimalFractionByUserName(String name);`
* `d` `Stream<Double> findDecimalFractionByNegativeSignFraction();`
* `e` `Fraction findFractionAdditionByUserId(String id);`
* `f` `Fraction findFractionSubtractionByUserName(String name);`

> Third release of the code (***v5.2.0-Release***).

#### 7. Bug (**1.5 pts**)

> Assume the 3rd search above is faulty and must be modified. Apply a change and perform the fourth release (***v5.2.1-Release***).

### :white_check_mark: Quality criteria (points may be deducted)

* Correct branched workflow usage. **Up to −3 pts**.
* Adequate temporal ordering of development as stated. **Up to −3 pts**.
* Code quality maintained via GitHub Actions and SonarCloud. Coverage ≥ 80%. **Up to −2 pts**.
* Adequate, complete and balanced management during development (estimation, real time, etc.). **Up to −2 pts**.
* Correct and complete commits. **Up to −2 pts**.
* Clean, well-formatted, well-ordered code. **Up to −2 pts**.
* English usage. **Up to −1 pt**.

### :clap: Submission

Provide the **GitHub URL** as text in the upload.

> **NOTE: remember to press the submit button.**

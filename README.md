# ⚡ Selenium E2E Framework

<div align="center">

![Build Verification](https://github.com/Gourab-Pal/selenium/actions/workflows/build-verification.yml/badge.svg?branch=master)
![E2E Tests](https://github.com/Gourab-Pal/selenium/actions/workflows/e2e-tests.yml/badge.svg?branch=master)
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4-43B02A?logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7-red?logo=testing-library&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-Kotlin_DSL-02303A?logo=gradle&logoColor=white)
![Allure](https://img.shields.io/badge/Allure-Report-brightgreen?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCI+PC9zdmc+)
![GitHub Pages](https://img.shields.io/badge/GitHub_Pages-Live-222?logo=github&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-3ECF8E?logo=supabase&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-Notifications-4A154B?logo=slack&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue)

**A production-grade Selenium automation framework with cross-browser execution, rich reporting, and a fully automated CI/CD pipeline.**

[📊 Live Report](https://gourab-pal.github.io/selenium/) · [⚙️ Actions](https://github.com/Gourab-Pal/selenium/actions) · [🐛 Issues](https://github.com/Gourab-Pal/selenium/issues)

</div>

---

## ✨ Features

| Feature | Description |
|---|---|
| 🌐 **Cross-Browser** | Runs tests in parallel on Chrome and Firefox |
| 📐 **Page Object Model** | Clean separation between test logic and UI interactions |
| 📊 **Allure Reports** | Rich HTML reports auto-published to GitHub Pages after every run |
| 🔁 **Retry Analyzer** | Automatically retries flaky tests before marking them failed |
| 📸 **Screenshot on Failure** | Captures and attaches browser screenshots to Allure on failure |
| 🗄️ **DB Tracking** | Persists every test run and result to Supabase (PostgreSQL) |
| 💬 **Slack Notifications** | Summary + failure alert sent to Slack channels post-execution |
| 📧 **Email Notifications** | Styled HTML email report sent via Gmail SMTP after every run |
| 🤖 **CI/CD Pipeline** | GitHub Actions with build verification, E2E execution, and publishing |
| 🔒 **Branch Protection** | `master` protected — all changes go through PRs with required CI gates |

---

## 🛠️ Tech Stack

<div align="center">

| Layer | Technology |
|---|---|
| ![Java](https://img.shields.io/badge/-Java_17-orange?logo=openjdk&logoColor=white) | Core language |
| ![Selenium](https://img.shields.io/badge/-Selenium_4-43B02A?logo=selenium&logoColor=white) | Browser automation |
| ![TestNG](https://img.shields.io/badge/-TestNG-red) | Test framework & parallel execution |
| ![Gradle](https://img.shields.io/badge/-Gradle_Kotlin_DSL-02303A?logo=gradle&logoColor=white) | Build tool |
| ![Allure](https://img.shields.io/badge/-Allure_Report-brightgreen) | Test reporting |
| ![GitHub Pages](https://img.shields.io/badge/-GitHub_Pages-222?logo=github&logoColor=white) | Report hosting |
| ![Supabase](https://img.shields.io/badge/-Supabase_PostgreSQL-3ECF8E?logo=supabase&logoColor=white) | Test run persistence |
| ![GitHub Actions](https://img.shields.io/badge/-GitHub_Actions-2088FF?logo=githubactions&logoColor=white) | CI/CD |
| ![Slack](https://img.shields.io/badge/-Slack_Webhooks-4A154B?logo=slack&logoColor=white) | Failure & summary alerts |
| ![Gmail](https://img.shields.io/badge/-Gmail_SMTP-EA4335?logo=gmail&logoColor=white) | Email notifications |

</div>

---

## 🏗️ Project Structure

```
selenium/
├── 📁 src/test/java/org/example/
│   ├── 📁 config/          # WebDriver factory & config loader
│   ├── 📁 core/            # BaseTest lifecycle & ThreadLocal DriverManager
│   ├── 📁 db/              # Supabase JDBC — test run & result persistence
│   ├── 📁 listeners/       # Retry analyzer, Allure browser-aware listener
│   ├── 📁 pages/           # Page Object classes (growing)
│   ├── 📁 tests/           # Test classes (growing)
│   └── 📁 utils/           # Allure helpers, screenshot capture, browser utils
│
├── 📁 src/test/resources/
│   ├── config.properties   # Base URLs & environment settings
│   ├── testng.xml          # TestNG suite definition
│   └── allure.properties   # Allure output config
│
├── 📁 .github/
│   ├── 📁 workflows/
│   │   ├── build-verification.yml   # Compile & validate on every PR/push
│   │   ├── e2e-tests.yml            # Orchestrator: test → publish → notify
│   │   ├── _run-tests.yml           # Reusable: parallel Chrome + Firefox matrix
│   │   ├── _publish-report.yml      # Reusable: Allure generate + GitHub Pages deploy
│   │   └── _notify-slack.yml        # Reusable: Slack summary + failure alert
│   └── 📁 email/
│       └── report-template.html     # Styled HTML email template
│
└── build.gradle.kts        # Dependencies & Allure plugin
```

---

## 🤖 CI/CD Pipeline

```
┌──────────────────────────┐
│   💡 Trigger             │  push · pull_request · schedule · manual
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│   🔨 Build Verification  │  Compile · Gradle wrapper validation
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│   🧪 Run Tests           │  Chrome ──┐
│   (parallel matrix)      │  Firefox──┴─► Allure results uploaded
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│   📊 Publish Report      │  Generate Allure → Deploy to GitHub Pages
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│   🔔 Notify              │  Slack summary + failure alert + Email report
└──────────────────────────┘
```

| Trigger | Build Verification | E2E Tests | Publish Report |
|---|---|---|---|
| Pull Request | ✅ | ✅ | ❌ |
| Push to `master` | ✅ | ✅ | ✅ |
| Daily Schedule (8:20 PM IST) | ❌ | ✅ | ✅ |
| Manual (`workflow_dispatch`) | ❌ | ✅ | ✅ |

---

## 🚀 Running Locally

### Prerequisites

- Java 17+
- Chrome and/or Firefox installed
- `./gradlew` wrapper included (no Gradle install needed)

### Commands

```bash
# Run all tests (default browser: chrome)
./gradlew clean test

# Run on a specific browser
BROWSER=chrome ./gradlew clean test
BROWSER=firefox ./gradlew clean test

# Run headless
HEADLESS=true BROWSER=chrome ./gradlew clean test

# Generate Allure report
./gradlew allureReport
# → open build/reports/allure-report/allureReport/index.html
```

```powershell
# Windows (PowerShell)
.\run-tests.ps1
```

---

## ⚙️ Configuration

Edit `src/test/resources/config.properties`:

```properties
saucedemo.url=https://www.saucedemo.com
theinternet.url=https://the-internet.herokuapp.com
environment=local
```

---

## 🔐 Required GitHub Secrets

| Secret | Purpose |
|---|---|
| `SUPABASE_DB_URL` | JDBC URL for Supabase PostgreSQL |
| `SUPABASE_DB_USER` | Database username |
| `SUPABASE_DB_PASSWORD` | Database password |
| `SLACK_WEBHOOK_URL` | Slack webhook — summary notifications |
| `SLACK_FAILURE_WEBHOOK_URL` | Slack webhook — failure-only alerts |
| `EMAIL_USERNAME` | Gmail address for sending reports |
| `EMAIL_PASSWORD` | Gmail app password |
| `TO_EMAIL` | Recipient email address |

---

## 🔒 Branch Protection (`master`)

| Rule | Status |
|---|---|
| Pull request required before merging | ✅ |
| Required checks: Build Verification + E2E (Chrome & Firefox) | ✅ |
| Branch must be up to date before merging | ✅ |
| Force pushes blocked | ✅ |
| Branch deletion blocked | ✅ |
| Admin bypass disabled | ✅ |

---

<div align="center">
  Made with ☕ and way too many browser windows
</div>

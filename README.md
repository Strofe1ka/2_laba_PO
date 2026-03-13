# Лабораторная работа №6 — Carsharing API

**Предмет:** PO6  
**Тема:** TLS/HTTPS, цепочка сертификатов, CI/CD

## Структура проекта

- `demo/` — Spring Boot приложение
- `demo/README.md` — подробная документация и инструкции по запуску

## Быстрый старт

```bash
cd demo
# 1. Создай application-local.properties из src/main/resources/application-local.properties.example
#    Укажи пароль PostgreSQL и TLS (после генерации сертификатов)
# 2. Сгенерируй сертификаты: ..\demo\scripts\generate-certificates.ps1 -StudentId "НОМЕР_СТУДБИЛЕТА"
mvn spring-boot:run
```

Приложение будет доступно по **https://localhost:8443**

Подробности — в [demo/README.md](demo/README.md).

## История коммитов

- **Лабораторная работа №6** — TLS/HTTPS, цепочка сертификатов, CI (GitHub Actions)
- **Лабораторная работа №5** — JWT, refresh tokens, сессии, расширенная аутентификация
- **Лабораторная работа №4** — Spring Security, CSRF, регистрация, роли, CRUD + бизнес-операции
- **Лабораторная работа №3** — REST API, PostgreSQL, JPA, CRUD

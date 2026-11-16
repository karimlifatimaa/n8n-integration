# n8n Integration — AI Error Alerts

A Spring Boot app that catches unhandled exceptions, saves them to PostgreSQL, and sends them to an n8n workflow. The workflow analyzes each error with OpenAI and posts the result to Telegram.

```
Exception ──► GlobalExceptionHandler ──┬──► PostgreSQL (logs)
                                       └──► n8n Webhook ──► OpenAI ──► Telegram
```

**Stack:** Java 17, Spring Boot 3.5 (Web, WebFlux, JPA), PostgreSQL 15, Lombok, Gradle, n8n

## Setup

1. **Start the database**
   ```bash
   docker compose up -d
   ```

2. **Import the workflow.** In n8n, import `n8n_workflow.json`, add your OpenAI and Telegram credentials, and replace `YOUR_CHAT_ID`.

3. **Configure** `src/main/resources/application.properties`:
   ```properties
   n8n.webhook.url=<your n8n webhook URL>
   ```

4. **Run**
   ```bash
   ./gradlew bootRun
   ```

## Test

```bash
curl http://localhost:8787/test-error
```

This returns a `500`, adds a row to the `logs` table, and sends a Telegram alert with the AI analysis.

## Payload

```json
{
  "service_name": "Backend",
  "error_message": "Simulated error for n8n integration testing!",
  "error_stacktrace": "java.lang.RuntimeException: ...",
  "timestamp": "2026-11-16T12:00:00"
}
```

> **Note:** a `webhook-test` URL only works while the n8n editor is listening for a test event. For regular use, activate the workflow and use the production URL.

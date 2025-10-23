# 🤖 Micro AI — Spring Boot + Kotlin + Ollama Microservice

A lightweight **AI-powered microservice** built with **Spring Boot (Kotlin)** that connects to **Ollama** models (like `qwen2.5:3b`) for general-purpose text generation and **AI-based CV analysis**.

---

## 🚀 Features

✅ Connects to **local or remote Ollama** API  
✅ Flexible prompt-based AI interaction (`/ai`)  
✅ Specialized **CV analysis endpoint** (`/ai/cvAnalisis`)  
✅ Unified API response model (`ApiResponse<T>`)  
✅ Robust **global exception handling**  
✅ Built-in **validation** with Jakarta annotations  
✅ Modular service/provider structure — ready for future AI providers

---

## 🧠 Architecture Overview

```
micro_ai/
├── configuration/
│   ├── GlobalExceptionHandler.kt      # Handles global REST errors
│   └── WebClientConfig.kt             # Configures WebClient for Ollama
│
├── controller/
│   └── Controller.kt                  # REST endpoints for AI and CV analysis
│
├── model/
│   ├── ApiResponse.kt                 # Generic unified response wrapper
│   ├── CvAnalysisResult.kt            # Model for CV analysis output
│   ├── OllamaResponse.kt              # Maps Ollama API JSON response
│   ├── RequestAi.kt                   # Request model for general AI
│   ├── RequestAiCv.kt                 # Request model for CV analysis
│   └── ResponseAi.kt                  # Simple text response wrapper
│
├── service/
│   ├── AiProvider.kt                  # Interface for AI providers
│   ├── AiService.kt                   # Main service orchestrator
│   └── OllamaService.kt               # Implementation for Ollama API
│
├── utils/
│   └── Utils.kt                       # Prompt builder for CV analysis
│
└── MicroAiApplication.kt              # Application entry point
```

---

## ⚙️ Tech Stack

| Layer | Technology |
|--------|-------------|
| Language | Kotlin |
| Framework | Spring Boot |
| HTTP Client | Spring WebFlux `WebClient` |
| Validation | Jakarta Validation (`@NotBlank`) |
| JSON Mapping | Jackson |
| AI Model | [Ollama](https://ollama.com) — local LLM API |
| Model Used | `qwen2.5:3b` (configurable) |

---

## 🧩 Configuration

Add your Ollama base URL to `application.yml` or `.properties`:

```yaml
base:
  url:
    ai: http://localhost:11434
```

Make sure your Ollama server is running and accessible:
```bash
ollama run qwen2.5:3b
```

---

## 🧾 API Endpoints

### 1️⃣ General AI Prompt

**POST** `/ai`

**Request Body:**
```json
{
  "prompt": "Write a poem about Kotlin developers"
}
```

**Response Example:**
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "response": "Kotlin developers code with grace..."
  }
}
```

---

### 2️⃣ CV Analysis

**POST** `/ai/cvAnalisis`

**Request Body:**
```json
{
  "cvText": "Najib Loera, Android developer with 5 years of experience...",
  "targetProfile": "Android Kotlin Developer"
}
```

**Response Example:**
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "candidate": {
      "name": "Najib Loera",
      "years_experience": 5,
      "summary": "Experienced Android Kotlin developer..."
    },
    "evaluation": {
      "technical_match": 8.5,
      "experience_match": 9,
      "overall_score": 8.7,
      "verdict": "Strong Candidate"
    },
    "key_points": {
      "strengths": ["Clean architecture", "Jetpack mastery"],
      "weaknesses": ["Limited backend exposure"],
      "recommended_questions": ["How do you handle concurrency in Kotlin?"]
    },
    "improvement_tips": [
      "Add recent projects to CV",
      "Highlight backend collaboration",
      "Quantify project impact"
    ]
  }
}
```

---

## ⚡ Error Handling

All responses use the same structure:

```json
{
  "status": 422,
  "message": "Validation error",
  "data": null,
  "error": "targetProfile: Target profile is required"
}
```

Handled globally by `GlobalExceptionHandler.kt`, including:
- 400 → Bad Request  
- 404 → Resource Not Found  
- 422 → Validation Error  
- 500 → Internal Server Error  

---

## 🧩 Example Usage

Send a request with **curl**:
```bash
curl -X POST http://localhost:8080/ai \
-H "Content-Type: application/json" \
-d '{"prompt":"Explain Kotlin coroutines simply"}'
```

Or for CV analysis:
```bash
curl -X POST http://localhost:8080/ai/cvAnalisis \
-H "Content-Type: application/json" \
-d '{"cvText":"...", "targetProfile":"Backend Developer"}'
```

---

## 🧰 Development

**Build & Run:**
```bash
./mvnw spring-boot:run
```

**Build JAR:**
```bash
./mvnw clean package
java -jar target/micro_ai-0.0.1-SNAPSHOT.jar
```

---

## 🧩 License

This project is open-source and available under the **MIT License**.

---

## 👨‍💻 Author

**Najib Alejandro Loera**  
💼 Software Developer | Android & Backend  
📧 alejandrorod35@gmail.com


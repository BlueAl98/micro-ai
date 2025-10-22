package com.blue.micro_ai.controller

import com.blue.micro_ai.model.ApiResponse
import com.blue.micro_ai.model.CvAnalysisResult
import com.blue.micro_ai.model.RequestAiCv
import com.blue.micro_ai.service.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/ai")
class Controller (
    private val aiService: AiService
){

    final val targetProfile = "Android Native Developer"
   // final val targetProfile = "Backend Developer"

    val promptTemplate = """
First of all, you are a strict JSON generator — always provide valid JSON format.

You are a senior technical recruiter and expert CV analyst.

Your task is to evaluate a candidate's CV based on a given Job Description for an **$targetProfile** position.

You must analyze and score the candidate in the following areas:
- Experience and background relevance
- Technical and soft skills alignment
- Overall candidate fit for the role

---

### OUTPUT RULES

- Always return a **single JSON object only** — no text, notes, or explanations before or after.
- Use **exactly** the JSON structure below.
- If any information is missing or unclear, leave the corresponding field **empty** ("" for strings, 0 for numbers, [] for arrays).
- You must **validate and debug your JSON twice** before returning it, ensuring:
  - Valid JSON syntax
  - All fields are present and properly typed

---

### JSON STRUCTURE (must always be identical)

{
  "candidate": {
    "name": "string",
    "years_experience": 0,
    "summary": "string"
  },
  "evaluation": {
    "technical_match": 0,
    "experience_match": 0,
    "overall_score": 0,
    "verdict": "string"
  },
  "key_points": {
    "strengths": ["string"],
    "weaknesses": ["string"],
    "recommended_questions": ["string"]
  },
  "improvement_tips": ["string"]
}

---

### FIELD INSTRUCTIONS

- **technical_match** → (1–10) Alignment with $targetProfile technical requirements
- **experience_match** → (1–10) Relevance of previous professional experience to the role
- **overall_score** → Weighted average (1–10)
- **verdict** → One of: `"Strong Candidate"`, `"Potential Candidate"`, `"Not a Fit"`
- **strengths** → 2–3 concise strengths backed by evidence
- **weaknesses** → 2–3 clear limitations or missing skills
- **recommended_questions** → 2–3 questions to verify uncertain or ambiguous areas
- **improvement_tips** → 3–5 short, actionable suggestions to improve the CV or alignment

---

### FINAL REQUIREMENT

At the end of your reasoning, **output only the final validated JSON object**, with no text or commentary outside of it.
""".trimIndent()


    val promt2 = "" +
            "First of all, you are a strict JSON generator always provides JSON FORMAT and" +
            "You are a senior technical recruiter and expert CV analyst.\n" +
            "\n" +
            "Your task is to evaluate a candidate's CV based on a given Job Description for an **${targetProfile}** position.\n" +
            "\n" +
            "You must analyze and score the candidate in the following areas:\n" +
            "- Experience and background relevance\n" +
            "- Technical and soft skills alignment\n" +
            "- Overall candidate fit for the role\n" +
            "\n" +
            "---\n" +
            "\n" +
            "### OUTPUT RULES\n" +
            "\n" +
            "- Always return a **single JSON object only** — no text, notes, or explanations before or after.\n" +
            "- Use **exactly** the JSON structure below. \n" +
            "- If any information is missing or unclear, leave the corresponding field **empty** (\"\" for strings, 0 for numbers, [] for arrays).\n" +
            "- You must **validate and debug your JSON twice** before returning it, ensuring:\n" +
            "  - Valid JSON syntax\n" +
            "  - All fields are present and properly typed\n" +
            "\n" +
            "---\n" +
            "\n" +
            "### JSON STRUCTURE (must always be identical)\n" +
            "\n" +
            "{\n" +
            "  \"candidate\": {\n" +
            "    \"name\": \"string\",\n" +
            "    \"years_experience\": 0,\n" +
            "    \"summary\": \"string\"\n" +
            "  },\n" +
            "  \"evaluation\": {\n" +
            "    \"technical_match\": 0,\n" +
            "    \"experience_match\": 0,\n" +
            "    \"overall_score\": 0,\n" +
            "    \"verdict\": \"string\"\n" +
            "  },\n" +
            "  \"key_points\": {\n" +
            "    \"strengths\": [\"string\"],\n" +
            "    \"weaknesses\": [\"string\"],\n" +
            "    \"recommended_questions\": [\"string\"]\n" +
            "  },\n" +
            "  \"improvement_tips\": [\"string\"]\n" +
            "}\n" +
            "\n" +
            "---\n" +
            "\n" +
            "### FIELD INSTRUCTIONS\n" +
            "\n" +
            "- **technical_match** → (1–10) Alignment with $targetProfile technical requirements\n" +
            "- **experience_match** → (1–10) Relevance of previous professional experience to the role\n" +
            "- **overall_score** → Weighted average (1–10)\n" +
            "- **verdict** → One of: `\"Strong Candidate\"`, `\"Potential Candidate\"`, `\"Not a Fit\"`\n" +
            "- **strengths** → 2–3 concise strengths $targetProfile by evidence\n" +
            "- **weaknesses** → 2–3 clear limitations or missing skills\n" +
            "- **recommended_questions** → 2–3 questions to verify uncertain or ambiguous areas\n" +
            "- **improvement_tips** → 3–5 short, actionable suggestions to improve the CV or alignment\n" +
            "\n" +
            "---\n" +
            "\n" +
            "### FINAL REQUIREMENT\n" +
            "\n" +
            "At the end of your reasoning, **output only the final validated JSON object**, with no text or commentary outside of it.\n"




    val cvText = "Najib Alejandro Loera\n" +
            "Rodriguez\n" +
            "DESARROLLADOR MOVIL (ANDROID KOTLIN SPECIALIST)\n" +
            "Ubicación: Durango, México | Teléfono: +52 675 115 3007\n" +
            "Correo: alejandrorod35@gmail.com | \uD83D\uDD17 LinkedIn | \uD83C\uDF10 Portafolio Web\n" +
            "Perfil Profesional\n" +
            "Desarrollador móvil con +3 años de experiencia, especializado en Android (Kotlin,\n" +
            "Java, Jetpack Compose) y con proyectos en iOS (Swift, SwiftUI). Experto en crear\n" +
            "aplicaciones escalables, seguras y de alto impacto, incluyendo sistemas críticos de\n" +
            "procesamiento en tiempo real. Conocimientos sólidos en arquitectura limpia,\n" +
            "Firebase, APIs REST, Docker y bases de datos móviles. Busco contribuir en equipos\n" +
            "internacionales de alto nivel tecnológico.\n" +
            "Education\n" +
            "Instituto Tecnológico de Durango\n" +
            "Ing. en Sistemas Computacionales – Especialidad en Ciencia de Datos (2016 – 2021)\n" +
            "CBTis No. 109\n" +
            "Técnico en Programación (2013 – 2016)\n" +
            "Experiencia Profesional\n" +
            "Desarrollador Móvil – Informática Electoral\n" +
            "Culiacán, México | 04/2023 – Presente\n" +
            "•\n" +
            "•\n" +
            "•\n" +
            "•\n" +
            "Desarrollo de apps críticas para procesos electorales, como PREPCASILLA,\n" +
            "logrando sincronización en tiempo real de +20,000 actas electorales en 8\n" +
            "estados durante 2024.\n" +
            "Implementación de un sistema de procesamiento de imágenes con OpenCV,\n" +
            "optimizando validación y recorte automático de documentos.\n" +
            "Creación de app multiplataforma (Android/iOS) para reservas turísticas, con\n" +
            "SwiftUI y Jetpack Compose, mejorando la experiencia del usuario en todo el\n" +
            "flujo de reserva.\n" +
            "Creación de Api rest para registro de reservas turísticas, con Spring Boot y\n" +
            "Kotlin\n" +
            "Desarrollador Android – Shitsu\n" +
            "Durango, México | 08/2021 – 04/2022\n" +
            "•\n" +
            "•\n" +
            "•\n" +
            "Diseño de app para registro de residencias universitarias, conectando\n" +
            "estudiantes con empresas.\n" +
            "Implementación de autenticación segura, carga de currículum y notificaciones\n" +
            "por correo automatizadas.\n" +
            "Creación de Api rest para respaldo de información de estudiantes con Spring\n" +
            "Boot y Java\n" +
            "Proyecto Freelance – Aplicación GYM\n" +
            "Durango, México | 07/2022 – 09/2022\n" +
            "•\n" +
            "•\n" +
            "•\n" +
            "App personalizada para rutinas de ejercicio y dietas.\n" +
            "Sistema de usuarios con progreso individual y notificaciones diarias de\n" +
            "entrenamiento.\n" +
            "Creación de Api rest para el registro de usuarios y rutinas con Spring Boot y\n" +
            "java\n" +
            "Idiomas\n" +
            "•\n" +
            "•\n" +
            "Español: Nativo\n" +
            "Inglés: Técnico Intermedio\n" +
            "Skills & abilities\n" +
            "●\n" +
            "●\n" +
            "●\n" +
            "●\n" +
            "Lenguajes: Kotlin (experto), Java (avanzado), Swift (intermedio), JavaScript (React).\n" +
            "Mobile: Jetpack Compose, SwiftUI, Firebase, SQLite, Clean Architecture, DaggerHilt.\n" +
            "Backend & Web: Spring Boot, REST APIs, ReactJS, Docker, MySql\n" +
            "Herramientas: Git, Scrum, CI/CD (básico)."


    val testPerson = "Marcos Rivera\n" +
            "marcos.rivera.fake@email.com\n" +
            " · +52 55 1234 5678 · Ciudad de México · github.com/marcos-rivera\n" +
            "\n" +
            "Resumen profesional\n" +
            "Desarrollador móvil con 4 años de experiencia en Android. Especialista en Kotlin, Jetpack Compose y arquitecturas limpias. Experiencia entregando apps con alto rendimiento, integración con backend REST y despliegues automatizados.\n" +
            "\n" +
            "Skills técnicos\n" +
            "\n" +
            "Lenguajes: Kotlin, Java, SQL\n" +
            "\n" +
            "UI: Jetpack Compose, XML, Material Design\n" +
            "\n" +
            "Arquitectura: Clean Architecture, MVVM, Coroutines, Flow\n" +
            "\n" +
            "Backend / APIs: REST, Retrofit, GraphQL (básico), JWT\n" +
            "\n" +
            "Testing: JUnit, MockK, Espresso\n" +
            "\n" +
            "DevOps / CI: GitHub Actions, Fastlane\n" +
            "\n" +
            "Infra / BaaS: Firebase (Auth, Firestore, Cloud Messaging), Google Play Console\n" +
            "\n" +
            "Herramientas: Android Studio, Gradle, Docker (básico)\n" +
            "\n" +
            "Metodologías: Agile / Scrum\n" +
            "\n" +
            "Experiencia profesional\n" +
            "Senior Android Developer — Soluciones Móviles S.A. · CDMX\n" +
            "Ene 2022 — Presente (3 años)\n" +
            "\n" +
            "Lideré el desarrollo de la app principal usando Kotlin + Jetpack Compose, aumentando el NPS en 18%.\n" +
            "\n" +
            "Rediseñé la arquitectura a Clean Architecture y reduje los bugs en producción en 40%.\n" +
            "\n" +
            "Implementé tests unitarios y de integración (coverage ~70%) y pipeline CI/CD con GitHub Actions + Fastlane.\n" +
            "\n" +
            "Integración con microservicios REST, manejo de autenticación con JWT y refresh token seguro.\n" +
            "\n" +
            "Android Developer — AppFast Tech · CDMX\n" +
            "Jul 2020 — Dic 2021 (1.5 años)\n" +
            "\n" +
            "Desarrollo de features críticos: pagos, notificaciones push y sincronización offline con Room + WorkManager.\n" +
            "\n" +
            "Optimización de consumo de batería y de tiempos de carga (startup time reducido 30%).\n" +
            "\n" +
            "Mentoría a 2 desarrolladores junior.\n" +
            "\n" +
            "Proyectos destacados\n" +
            "\n" +
            "PagoFácil (app de pagos) — Implementé flujo de pagos, integración con proveedores y validación de seguridad (PCI-aware).\n" +
            "\n" +
            "FitBuddy — App social de fitness; implementé sincronización offline y modelo de datos eficiente con Room + Flow.\n" +
            "\n" +
            "Educación\n" +
            "Ingeniería en Sistemas Computacionales — Universidad Tecnológica de la Ciudad\n" +
            "2016 — 2020\n" +
            "\n" +
            "Certificaciones\n" +
            "\n" +
            "Google Associate Android Developer (ficticio para prueba)\n" +
            "\n" +
            "Curso avanzado Jetpack Compose — Plataforma Online\n" +
            "\n" +
            "Idiomas\n" +
            "\n" +
            "Español (nativo)\n" +
            "\n" +
            "Inglés (intermedio — lectura técnica y comunicación básica)"


    val backendtest = "Nombre: Diego Hernández\n" +
            "Correo: diego.hernandez.fake@email.com\n" +
            "\n" +
            "Teléfono: +52 55 4321 8765\n" +
            "Ubicación: Guadalajara, México\n" +
            "GitHub: github.com/diego-hdev\n" +
            "LinkedIn: linkedin.com/in/diegoh-dev\n" +
            "\n" +
            "Perfil profesional\n" +
            "\n" +
            "Desarrollador backend con 5 años de experiencia construyendo APIs escalables y microservicios en Java, Kotlin y Spring Boot. Experiencia en diseño de arquitectura limpia, integración con bases de datos SQL/NoSQL y despliegues en contenedores con Docker y Kubernetes. Apasionado por la automatización, el testing y la observabilidad.\n" +
            "\n" +
            "Habilidades técnicas\n" +
            "\n" +
            "Lenguajes: Kotlin, Java, Python (básico)\n" +
            "\n" +
            "Frameworks: Spring Boot, Micronaut, Ktor\n" +
            "\n" +
            "Bases de datos: PostgreSQL, MongoDB, Redis\n" +
            "\n" +
            "Mensajería / Asíncrono: RabbitMQ, Kafka\n" +
            "\n" +
            "Seguridad: OAuth2, JWT, Spring Security\n" +
            "\n" +
            "Testing: JUnit 5, Mockito, Testcontainers\n" +
            "\n" +
            "DevOps: Docker, Kubernetes, GitHub Actions, CI/CD\n" +
            "\n" +
            "Infraestructura: AWS (EC2, RDS, S3), GCP (Cloud Run, Pub/Sub)\n" +
            "\n" +
            "Metodologías: Agile / Scrum\n" +
            "\n" +
            "Experiencia profesional\n" +
            "\n" +
            "Senior Backend Developer — Fintrack Systems · Remoto\n" +
            "Ene 2022 – Presente\n" +
            "\n" +
            "Desarrollé microservicios financieros con Spring Boot y Kotlin para el procesamiento de transacciones en tiempo real.\n" +
            "\n" +
            "Implementé autenticación JWT y autorización basada en roles.\n" +
            "\n" +
            "Migré servicios monolíticos a contenedores Docker orquestados con Kubernetes.\n" +
            "\n" +
            "Integré observabilidad con Prometheus y Grafana, mejorando la detección de incidentes en 35%.\n" +
            "\n" +
            "Diseñé endpoints REST y GraphQL documentados con Swagger y OpenAPI.\n" +
            "\n" +
            "Backend Developer — SoftCloud Solutions · Guadalajara, MX\n" +
            "Ago 2019 – Dic 2021\n" +
            "\n" +
            "Desarrollé APIs REST en Java + Spring Boot para aplicaciones de e-commerce y CRM.\n" +
            "\n" +
            "Implementé cacheo con Redis para mejorar tiempos de respuesta un 40%.\n" +
            "\n" +
            "Diseñé pipelines CI/CD usando GitHub Actions y SonarQube.\n" +
            "\n" +
            "Colaboré con equipos frontend y QA para mejorar integración y cobertura de pruebas.\n" +
            "\n" +
            "Proyectos destacados\n" +
            "\n" +
            "InvoiceHub API: Microservicio de facturación en Kotlin con PostgreSQL, JWT y Docker.\n" +
            "\n" +
            "TrackLog Service: Sistema de tracking logístico usando Kafka + MongoDB.\n" +
            "\n" +
            "Educación\n" +
            "\n" +
            "Ingeniería en Software — Universidad de Guadalajara\n" +
            "2015 – 2019\n" +
            "\n" +
            "Certificaciones\n" +
            "\n" +
            "Spring Professional Certification (ficticio)\n" +
            "\n" +
            "AWS Certified Developer – Associate (ficticio)\n" +
            "\n" +
            "Idiomas\n" +
            "\n" +
            "Español (nativo)\n" +
            "\n" +
            "Inglés (intermedio-avanzado)"


    @PostMapping
    fun generateCvAnalisis(
        @RequestBody request: RequestAiCv
    ): ApiResponse<CvAnalysisResult> {

        // 🧠 Combine the system/base prompt with the candidate CV text
        val finalPrompt = """
        ${promptTemplate}

        Candidate CV:
        ${request.cvText}
        """.trimIndent()

        return aiService.getAiData(
            prompt = finalPrompt,
            responseType = CvAnalysisResult::class.java,
            providerName = "ollamaProvider"
        )
    }


}





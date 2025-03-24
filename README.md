# AI Scenario Analysis Tool

A full-stack application that uses Ollama's Mistral model to analyze scenarios and provide structured insights, including potential pitfalls, strategies, and recommended resources.

## Features

- 🤖 AI-powered scenario analysis using Ollama (Mistral model)
- 🎯 Dynamic constraint management
- 💅 Material-UI components with custom theming
- ⚡ Real-time validation and error handling
- 🔄 Loading states and animations
- 📱 Responsive design

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.3
- OkHttp3 for API calls
- Lombok for boilerplate reduction
- Spring Validation

### Frontend
- React 18
- Material-UI v5
- Axios for API calls
- Custom theme configuration
- Emotion for styled components

## Prerequisites

- Java 17 or later
- Node.js 14 or later
- Ollama installed and running
- Maven (or use included wrapper)

## Quick Start

### Backend Setup

1. Start Ollama service:
```bash
ollama serve
```

2. Pull the Mistral model:
```bash
ollama pull mistral
```

3. Build and run the Spring Boot application:
```bash
# Using Maven
mvn spring-boot:run

# Or using Maven wrapper
./mvnw spring-boot:run
```

### Frontend Setup

1. Navigate to frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

## API Documentation

### Analyze Scenario Endpoint

```http
POST /api/analyze-scenario
Content-Type: application/json

Request Body:
{
  "scenario": "string",
  "constraints": ["string"]
}

Response Body:
{
  "scenarioSummary": "string",
  "potentialPitfalls": ["string"],
  "proposedStrategies": ["string"],
  "recommendedResources": ["string"],
  "disclaimer": "string"
}
```

## Configuration

### Backend Configuration (application.properties)
```properties
server.port=8081
ollama.api.url=http://localhost:11434/api/generate
```

## Project Structure

```
.
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/scenarioanalyzer/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── model/
│       │       ├── service/
│       │       └── exception/
│       └── resources/
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   └── App.jsx
│   └── public/
└── pom.xml
```

## Error Handling

- Backend validation using Spring Validation
- Global exception handling
- Frontend loading states
- User-friendly error messages
- CORS configuration
- API timeout handling

## Development Scripts

### Backend
```bash
# Run tests
mvn test

# Clean and install
mvn clean install

# Run application
mvn spring-boot:run
```

### Frontend
```bash
# Start development server
npm start

# Build for production
npm run build

# Run tests
npm test
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

[MIT](LICENSE)

## Acknowledgments

- Ollama team for providing the Mistral model
- Spring Boot team
- React and Material-UI teams
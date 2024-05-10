### Trino AI Functions Plugin (Experimental)

The Trino AI Functions Plugin is an experimental extension for the Trino SQL engine, designed to integrate advanced AI functionalities directly within SQL queries. This plugin utilizes state-of-the-art machine learning models from OpenAI to provide powerful text processing capabilities, such as sensitive data masking and text analysis, within a familiar SQL environment.

**Use Cases:**

- **Data Anonymization:** Quickly anonymize datasets by masking out personal information, making it easier to share data across teams or with external partners without compromising individual privacy.
- **Enhanced Data Analysis:** Use AI-driven text analysis to extract insights, summarize texts, or even translate data on-the-fly during SQL querying, enriching data analysis without additional data processing steps.

**Important Considerations:**

- **Experimental Status:** This plugin is currently in an experimental phase. It should be used with caution, as it may undergo significant changes and could potentially impact system stability and data integrity.
- **Security and Compliance:** Given the experimental nature of this plugin, thorough testing and validation are recommended before deploying in production environments, especially in use cases handling sensitive or regulated data.
- **Performance Impact:** The use of AI models within SQL queries may introduce additional latency and resource consumption, depending on the complexity of the model and the size of the dataset. Performance testing is advised to evaluate the impact on query execution times and system resources.
- **Model Availability:** The plugin requires access to LLM (Large Language Model) models from AI providers such as OpenAI. Ensure that you have the necessary API keys and permissions to access these models before using the plugin., 

**Note:** As this is an experimental tool, it is recommended to use this plugin in test environments and to regularly back up your data.

## Getting Started

Follow these instructions to set up the project locally for development and testing purposes.

### Prerequisites

- Trino 447
- Java 22
- Maven
- API Key
- API Model
- API Endpoint
- Optional: Ollama Server

### Building

To set up your development environment, execute the following commands:

```bash
# Clone the repository
git clone https://github.com/alaturqua/trino-ai.git

# Navigate into the project directory
cd trino-ai

# Build the project
./mvnw clean install
```

### Installing
After building copy the `target/trino-ai-<version>` folder to the plugin directory of Trino.


## Docker Deployment

Deploy the project using Docker and Docker Compose, as defined in the `docker-compose.yml` file in the root directory.

### Prerequisites

- Docker
- Docker Compose
- API Key
- API Model
- API Endpoint
- Optional: Ollama Server

### Configuration

Create a `.env` file in the root directory with the necessary API variables:

```env
API_MODEL=<your_model>
API_KEY=<your_api_key>
API_ENDPOINT=<your_api_endpoint>
```
Replace `<your_model>`, `<your_api_key>`, and `<your_api_endpoint>` with your actual values.

### Running the Project

Run the project using Docker Compose with the following command:

```bash
docker-compose up -d
```

### Local LLM Server - Ollama

For local deployments, you may use the Ollama Server. Refer to the [Ollama Repository](https://github.com/ollama/ollama) for setup instructions.

#### Linux Installation

```bash
curl -fsSL https://ollama.com/install.sh | sh
```

### Quickstart

#### llama3
```bash
ollama pull llama3
```

#### mistral
```bash
ollama pull mistral
```

#### phi3
```bash
ollama pull phi3
```
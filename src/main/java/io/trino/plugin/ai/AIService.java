/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.logging.Logger;

public class AIService
{
    private static final String ENDPOINT = System.getenv("API_ENDPOINT");  //"https://api.openai.com/v1/chat/completions";
    private static final Logger log = Logger.getLogger(AIService.class.getCanonicalName());
    private final AIClient client;
    private final String model;
    private final double temperature;
    private final int maxTokens;

    public AIService(String model, double temperature, int maxTokens)
    {
        this.client = new AIClient();  // Assumes API key is set within AIClient using System.getenv()
        this.model = model;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        log.info("AI Service initialized with model: " + model + ", temperature: " + temperature + ", max tokens: " + maxTokens);
    }

    public String getCompletion(String prompt, String systemInstruction)
            throws IOException
    {
        ChatRequest chatRequest = new ChatRequest(prompt, systemInstruction, model, temperature, maxTokens);
        String json = chatRequest.toJson();
        String response = client.callAPI(ENDPOINT, json);
        log.info("Response: " + response);
        return processResponse(response);
    }

    private String processResponse(String jsonResponse)
            throws IOException
    {
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.has("error")) {
            throw new IOException("Error from AI: " + jsonObject.getAsJsonObject("error").get("message").getAsString());
        }

        JsonObject usage = jsonObject.getAsJsonObject("usage");
        int promptTokens = usage.get("prompt_tokens").getAsInt();
        int completionTokens = usage.get("completion_tokens").getAsInt();
        int totalTokens = usage.get("total_tokens").getAsInt();

        double inputCost = calculateCost(promptTokens, 0.50);  // $0.50 per 1M input tokens
        double outputCost = calculateCost(completionTokens, 1.50);  // $1.50 per 1M output tokens

        log.info("Usage - Prompt Tokens: " + promptTokens + ", Completion Tokens: " + completionTokens + ", Total Tokens: " + totalTokens);
        log.info("Cost - Input Cost: $" + String.format("%.4f", inputCost) + ", Output Cost: $" + String.format("%.4f", outputCost));

        return jsonObject.getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("message")
                .get("content")
                .getAsString().trim();
    }

    private double calculateCost(int tokens, double pricePerMillion)
    {
        return (tokens / 1_000_000.0) * pricePerMillion;
    }
}

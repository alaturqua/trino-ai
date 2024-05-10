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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatRequest
{
    private static final Logger log = Logger.getLogger(ChatRequest.class.getCanonicalName());
    final List<Message> messages;
    final String model;
    final double temperature;
    @SerializedName("max_tokens") final int maxTokens;
    final String mode;

    public ChatRequest(String prompt, String systemInstruction, String model, double temperature, int maxTokens)
    {
        this.model = System.getenv("API_MODEL") != null ? System.getenv("API_MODEL") : model;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.messages = new ArrayList<>();
        this.mode = "instruct";

        String defaultSystemInstruction = "You are a helpful Assistant. Return only the result for this query. DO NOT provide any additional information.";

        // Optionally add a system message if an instruction is provided
        if (systemInstruction != null && !systemInstruction.isEmpty()) {
            this.messages.add(new Message("system", defaultSystemInstruction + " " + systemInstruction));
        }
        else {
            this.messages.add(new Message("system", defaultSystemInstruction));
        }

        // Add the user message
        this.messages.add(new Message("user", prompt));
    }

    public String toJson()
    {
        Gson gson = new GsonBuilder().create();
        log.info(gson.toJson(this));
        return gson.toJson(this);
    }

    /**
     * @param role 'user' or 'system'
     */
    record Message(String role, String content) {}
}

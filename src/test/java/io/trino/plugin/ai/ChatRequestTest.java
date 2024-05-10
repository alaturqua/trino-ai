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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatRequestTest
{
    @Test
    public void testToJson()
    {
        // Create a ChatRequest object with sample data
        ChatRequest chatRequest = new ChatRequest("Hello", "Please provide some information", "gpt-3.5-turbo", 0.8, 100);

        // Call the toJson() method
        String json = chatRequest.toJson();

        // Assert that the JSON string is not null
        assertNotNull(json);

        // Assert that the JSON string contains the expected fields
        assertTrue(json.contains("messages"));
        assertTrue(json.contains("model"));
        assertTrue(json.contains("temperature"));
        assertTrue(json.contains("max_tokens"));
    }

    @Test
    public void testMessageRecord()
    {
        // Create a Message object with sample data
        ChatRequest.Message message = new ChatRequest.Message("user", "Hello");

        // Assert that the role and content fields are set correctly
        assertEquals("user", message.role());
        assertEquals("Hello", message.content());
    }
}

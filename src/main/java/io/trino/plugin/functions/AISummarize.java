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

package io.trino.plugin.functions;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import io.trino.plugin.ai.AIService;
import io.trino.spi.function.Description;
import io.trino.spi.function.ScalarFunction;
import io.trino.spi.function.SqlNullable;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.StandardTypes;

import java.io.IOException;

public class AISummarize
{
    private static final AIService service = new AIService(
            null,
            0.5,
            50);

    private AISummarize() {}

    @Description("Generate a concise summary of the provided text")
    @ScalarFunction("ai_summarize")
    @SqlType(StandardTypes.VARCHAR)
    @SqlNullable
    public static Slice aiSummarize(@SqlType(StandardTypes.VARCHAR) Slice input)
            throws IOException
    {
        if (input == null || input.length() == 0) {
            return null;
        }

        String prompt = "Be concise and return only what you were asked. Please summarize this text with max 50 words: " + input.toStringUtf8();
        String jsonResponse = service.getCompletion(prompt, "");  // Ensure the max_tokens is appropriate
        return Slices.utf8Slice(jsonResponse);
    }
}

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
import java.util.Locale;

public final class AIAnalyzeSentiment
{
    private static final AIService service = new AIService(
            null,
            0.0,
            60);

    private AIAnalyzeSentiment() {}

    @Description("Analyze the sentiment of the provided text")
    @ScalarFunction("ai_analyze_sentiment")
    @SqlType(StandardTypes.VARCHAR)
    @SqlNullable
    public static Slice aiAnalyzeSentiment(@SqlType(StandardTypes.VARCHAR) Slice input)
            throws IOException
    {
        if (input == null || input.length() == 0) {
            return null;
        }

        String instruction = "Analyze and identify the sentiment of the whole input is positive, negative, or neutral. Respond only with one of these three options: 'positive, negative, neutral'. Input: ";
        String response = service.getCompletion(instruction + input.toStringUtf8(), "");
        // String sanitizedResponse = sanitizeResponse(response.toLowerCase(Locale.ROOT));  // Apply regex check
        return Slices.utf8Slice(response.toLowerCase(Locale.ROOT));
    }

//    private static String sanitizeResponse(String response)
//    {
//        if (response.matches("^(positive|negative|neutral)$")) {
//            return response;
//        }
//        else {
//            return "neutral";  // Default safe response
//        }
//    }
}

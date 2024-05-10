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

public final class AILanguageDetection
{
    private static final AIService service = new AIService(
            null,
            0.0,             // Low temperature
            60);

    private AILanguageDetection() {}

    @Description("Detect the language of the provided text")
    @ScalarFunction("ai_detect_language")
    @SqlType(StandardTypes.VARCHAR)
    @SqlNullable
    public static Slice aiDetectLanguage(@SqlType(StandardTypes.VARCHAR) Slice input)
            throws IOException
    {
        if (input == null || input.length() == 0) {
            return null;
        }

        String prompt = "Detect the language of the following text and respond with " +
                "language code only e.g. 'en' for English, " +
                "'de' for German, 'fr' for French etc: " + input.toStringUtf8();
        String response = service.getCompletion(prompt, "");
        return Slices.utf8Slice(response);
    }
}

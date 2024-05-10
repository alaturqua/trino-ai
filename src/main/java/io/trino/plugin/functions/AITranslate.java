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

public class AITranslate
{
    private static final AIService service = new AIService(
            null,
            0.0,
            50);

    private AITranslate() {}

    @Description("Translate the provided text to the target language e.g. ['en', 'es', 'de'] ai_translate('Hello', 'es')")
    @ScalarFunction("ai_translate")
    @SqlType(StandardTypes.VARCHAR)
    @SqlNullable
    public static Slice aiTranslate(@SqlType(StandardTypes.VARCHAR) Slice input, @SqlType(StandardTypes.VARCHAR) Slice targetLanguage)
            throws IOException
    {
        if (input == null || input.length() == 0 || targetLanguage == null || targetLanguage.length() == 0) {
            return null;
        }

        String prompt = String.format("Translate the provided text to the target language e.g. 'en', 'es', 'de' text: %s || target language:%s", targetLanguage.toStringUtf8(), input.toStringUtf8());
        String jsonResponse = service.getCompletion(prompt, "");  // Ensure the max_tokens is appropriate
        return Slices.utf8Slice(jsonResponse);
    }
}

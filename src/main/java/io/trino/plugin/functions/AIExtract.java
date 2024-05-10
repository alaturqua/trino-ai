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
import io.trino.spi.block.Block;
import io.trino.spi.function.Description;
import io.trino.spi.function.ScalarFunction;
import io.trino.spi.function.SqlNullable;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.StandardTypes;
import io.trino.spi.type.VarcharType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class AIExtract
{
    private static final AIService service = new AIService(
            null,
            0.0,
            150);

    private AIExtract()
    {
    }

    @Description("Extract specified items from the input")
    @ScalarFunction("ai_extract")
    @SqlType(StandardTypes.VARCHAR)
    @SqlNullable
    public static Slice aiExtract(
            @SqlType(StandardTypes.VARCHAR) Slice input,
            @SqlType("array(varchar)") Block keywordsBlock)
            throws IOException
    {
        // Check for null or empty input immediately and return null to avoid processing.
        if (input == null || input.length() == 0 || keywordsBlock == null || keywordsBlock.getPositionCount() == 0) {
            return null;
        }

        // Convert Block to Strings
        List<String> keywords = new ArrayList<>();
        for (int i = 0; i < keywordsBlock.getPositionCount(); i++) {
            Slice keyword = VarcharType.VARCHAR.getSlice(keywordsBlock, i);
            keywords.add(keyword.toStringUtf8());
        }

        String prompt = buildExtractionPrompt(input.toStringUtf8(), keywords);
        String response = service.getCompletion(prompt, "");
        return Slices.utf8Slice(response);
    }

    private static String buildExtractionPrompt(String text, List<String> keywords)
    {
        StringJoiner joiner = new StringJoiner(", ");
        for (String keyword : keywords) {
            joiner.add(keyword);
        }
        return String.format("Find and extract the following matching items from the text: %s." +
                "Return only the extracted values without keys as a comma-separated list. Text: %s", joiner, text);
    }
}

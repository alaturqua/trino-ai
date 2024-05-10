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

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class AIClient
{
    private final String apiKey;
    private final OkHttpClient client = new OkHttpClient();

    public AIClient()
    {
        String apiKey = System.getenv("API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("API Key must be set in the 'API_KEY' environment variable and cannot be empty");
        }
        this.apiKey = apiKey;
    }

    public AIClient(String apiKey)
    {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("API Key must be set and cannot be empty");
        }
        this.apiKey = apiKey;
    }

    public String callAPI(String endpoint, String json)
            throws IOException
    {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(endpoint)
                .header("Content-Type", "application/json")
                // .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " with body " + response.body().string());
            }
            return response.body().string();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

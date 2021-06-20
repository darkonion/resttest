package resttask.httpClient;


import okhttp3.OkHttpClient;

import java.time.Duration;


public class HttpClientConfiguration {

    private static OkHttpClient httpClient;
    private static final long TIMEOUT = 10L;

    private HttpClientConfiguration() { }

    private static void setupHttpClient() {
        httpClient = new OkHttpClient().newBuilder()
                .callTimeout(Duration.ofSeconds(TIMEOUT))
                .connectTimeout(Duration.ofSeconds(TIMEOUT))
                .readTimeout(Duration.ofSeconds(TIMEOUT))
                .addInterceptor(new HttpLoggingInterceptor())
                .retryOnConnectionFailure(true)
                .build();
    }

    //Multi thread unsafe!
    public static OkHttpClient getHttpClient() {
        if (httpClient == null) setupHttpClient();
        return httpClient;
    }
}
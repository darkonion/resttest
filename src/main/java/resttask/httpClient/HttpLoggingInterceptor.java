package resttask.httpClient;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

public class HttpLoggingInterceptor implements Interceptor {

    private static final Logger LOGGER = Logger.getLogger(HttpLoggingInterceptor.class.getSimpleName());

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        LOGGER.log(Level.INFO,
                () -> format("---> New request url: %s, %n Headers %s", request.url(), request.headers()));

        Response response = chain.proceed(request);

        LOGGER.log(Level.INFO,
                () -> format("<--- Response recieved, code: %d, %n Headers %s", response.code(), response.headers()));

        return response;
    }
}

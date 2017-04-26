package io.github.cs407_chatby.chatby.data;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final AuthHolder authHolder;

    @Inject
    public AuthInterceptor(AuthHolder authHolder) {
        this.authHolder = authHolder;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = authHolder.readToken();
        Request request = chain.request();
        if (token != null) {
            request = request.newBuilder()
                    .header("Authorization", "Token " + authHolder.readToken())
                    .build();
        }
        return chain.proceed(request);
    }
}

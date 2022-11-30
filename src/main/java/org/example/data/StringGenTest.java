package org.example.data;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public class StringGenTest implements IStringGen {

    @Override
    @Async
    public CompletableFuture<String> sayHello() {
        return CompletableFuture.completedFuture("TEST");
    }
}

package org.example.data;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public class StringGenDev implements IStringGen {

    @Async
    @Override
    public CompletableFuture<String> sayHello() {
        CompletableFuture<String> result = null;
        try {
            System.out.println("Asynchronous thread name: " + Thread.currentThread().getName());
            Thread.sleep(5000);
            result = CompletableFuture.completedFuture("DEVELOPMENT");
        } catch (InterruptedException e){}
        return result;
    }
}

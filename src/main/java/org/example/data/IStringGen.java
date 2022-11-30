package org.example.data;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface IStringGen {
    CompletableFuture<String> sayHello();
}

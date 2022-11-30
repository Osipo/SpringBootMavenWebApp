package org.example.controllers.interceptors;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.servlet.AsyncHandlerInterceptor;


import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("security_context_async_interceptor")
public class SecurityContextAsyncInterceptor implements AsyncHandlerInterceptor, DeferredResultProcessingInterceptor {

    private final Map<HttpServletResponse, SecurityContext> cache = new ConcurrentHashMap<>();


    //Save context from Thread T1 before Async calling.
    @Override
    public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult) {
        SecurityContext context = SecurityContextHolder.getContext();
        cache.put(request.getNativeResponse(HttpServletResponseWrapper.class), context);
    }

    //Set saved context at T2 async thread.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (request.getDispatcherType() == DispatcherType.ASYNC) {
            SecurityContext context = cache.get(((HttpServletResponseWrapper) response).getResponse());
            if (context != null) {
                SecurityContextHolder.setContext(context);
            }
        }
        return true;
    }

    //after completion remove saved context.
    @Override
    public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) {
        cache.remove(request.getNativeResponse(HttpServletResponseWrapper.class).getResponse());
    }
}

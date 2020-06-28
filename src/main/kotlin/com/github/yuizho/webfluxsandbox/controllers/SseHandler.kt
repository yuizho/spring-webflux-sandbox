package com.github.yuizho.webfluxsandbox.controllers

import com.github.yuizho.webfluxsandbox.domain.Message
import com.github.yuizho.webfluxsandbox.domain.QueueOperation
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

class SseHandler(private val queueOperation: QueueOperation) {
    fun post(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<Message>()
                .flatMap { queueOperation.push(it) }
                .flatMap { ServerResponse.ok().body(Mono.just(it), Message::class.java) }
    }

    fun channel(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse
                .ok()
                .body(Flux.interval(Duration.ofMillis(1000)).take(300)
                        .flatMap { queueOperation.pop() }
                        .log()
                        .map { message ->
                            ServerSentEvent.builder(message).event("message").build()
                        },
                        ServerSentEvent::class.java
                )
    }
}
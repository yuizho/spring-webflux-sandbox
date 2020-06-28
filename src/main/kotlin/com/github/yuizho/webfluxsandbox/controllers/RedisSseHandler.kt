package com.github.yuizho.webfluxsandbox.controllers

import com.github.yuizho.webfluxsandbox.domain.ChannelMessage
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class RedisSseHandler(private val reactiveRedisOperations: ReactiveRedisOperations<String, ChannelMessage>) {
    companion object {
        const val REDIS_CHANNEL = "message"
    }

    fun post(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<ChannelMessage>()
                .flatMap { reactiveRedisOperations.convertAndSend(REDIS_CHANNEL, it) }
                .flatMap { ServerResponse.ok().body(Mono.just("pushed to redis"), String::class.java) }
    }

    fun channel(request: ServerRequest): Mono<ServerResponse> {
        val channelId = request.pathVariable("id")
        return ServerResponse
                .ok()
                .body(reactiveRedisOperations.listenToChannel(REDIS_CHANNEL)
                        .map { it.message }
                        .log("before filtering on channel $channelId")
                        .filter { channelId == it.to }
                        .log("after filtering on channel $channelId")
                        .map { message ->
                            ServerSentEvent.builder(message).event("message").build()
                        },
                        ServerSentEvent::class.java
                )
    }
}
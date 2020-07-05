package com.github.yuizho.webfluxsandbox.controllers

import com.github.yuizho.webfluxsandbox.domain.ChannelMessage
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.connection.stream.StreamRecords
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class RedisStreamSseHandler(
        private val reactiveRedisOperations: ReactiveRedisOperations<String, ChannelMessage>,
        private val streamReceiver: StreamReceiver<String, MapRecord<String, String, String>>
) {
    companion object {
        const val REDIS_CHANNEL = "message"
    }

    fun post(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<ChannelMessage>()
                .flatMap {
                    Mono.just(
                            StreamRecords.newRecord()
                                    .`in`(REDIS_CHANNEL)
                                    .ofObject(it)
                    )
                }
                .flatMap { reactiveRedisOperations.opsForStream<String, ChannelMessage>().add(it) }
                .flatMap { ServerResponse.ok().body(Mono.just("pushed to redis"), String::class.java) }
    }

    fun channel(request: ServerRequest): Mono<ServerResponse> {
        val channelId = request.pathVariable("id")
        return ServerResponse
                .ok()
                .body(streamReceiver.receive(StreamOffset.fromStart(REDIS_CHANNEL))
                        .map { ChannelMessage(it.value.get("to")!!, it.value.get("value")!!) }
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
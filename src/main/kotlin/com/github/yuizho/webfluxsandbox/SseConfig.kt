package com.github.yuizho.webfluxsandbox

import com.github.yuizho.webfluxsandbox.controllers.SseHandler
import com.github.yuizho.webfluxsandbox.domain.QueueOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class SseConfig {
    @Bean
    fun routes(sseHandler: SseHandler): RouterFunction<ServerResponse> {
        return router {
            GET("/channel") { sseHandler.channel(it) }
            POST("/post") { sseHandler.post(it) }
        }
    }

    @Bean
    fun sseHandler(queueOperation: QueueOperation): SseHandler {
        return SseHandler(queueOperation)
    }
}
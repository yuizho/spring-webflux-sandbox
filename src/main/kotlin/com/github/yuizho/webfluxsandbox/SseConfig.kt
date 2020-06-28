package com.github.yuizho.webfluxsandbox

import com.github.yuizho.webfluxsandbox.controllers.SimpleSseHandler
import com.github.yuizho.webfluxsandbox.domain.QueueOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class SseConfig {
    @Bean
    fun routes(simpleSseHandler: SimpleSseHandler): RouterFunction<ServerResponse> {
        return router {
            GET("/simple/channel") { simpleSseHandler.channel(it) }
            POST("/simple/post") { simpleSseHandler.post(it) }
        }
    }

    @Bean
    fun simpleSseHandler(queueOperation: QueueOperation): SimpleSseHandler {
        return SimpleSseHandler(queueOperation)
    }
}
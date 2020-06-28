package com.github.yuizho.webfluxsandbox

import com.github.yuizho.webfluxsandbox.controllers.RedisSseHandler
import com.github.yuizho.webfluxsandbox.controllers.SimpleSseHandler
import com.github.yuizho.webfluxsandbox.domain.ChannelMessage
import com.github.yuizho.webfluxsandbox.domain.QueueOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
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
    fun redisRoutes(redisSseHandler: RedisSseHandler): RouterFunction<ServerResponse> {
        return router {
            GET("/redis/channel/{id}") { redisSseHandler.channel(it) }
            POST("/redis/post") { redisSseHandler.post(it) }
        }
    }

    @Bean
    fun simpleSseHandler(queueOperation: QueueOperation): SimpleSseHandler {
        return SimpleSseHandler(queueOperation)
    }

    @Bean
    fun redisSseHandler(reactiveRedisOperations: ReactiveRedisOperations<String, ChannelMessage>): RedisSseHandler {
        return RedisSseHandler(reactiveRedisOperations)
    }

    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, ChannelMessage> {
        val context = RedisSerializationContext
                // the serializer of key
                .newSerializationContext<String, ChannelMessage>(StringRedisSerializer())
                // the serializer of value
                .value(Jackson2JsonRedisSerializer(ChannelMessage::class.java))
                .build()
        return ReactiveRedisTemplate(factory, context)
    }
}
package com.github.yuizho.webfluxsandbox.controllers

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
class DemoController {
    @GetMapping("/hello")
    fun hello(): Mono<String> {
        return Mono.just("Hello world")
    }

    @GetMapping("/hellos")
    fun hellos(): Flux<Bean> {
        return Flux.range(1, 5)
                .map { Bean("Hello world $it") }
                .delayElements(Duration.ofSeconds(1))
    }
}

data class Bean(
        @field:JsonProperty("message")
        val message: String
)
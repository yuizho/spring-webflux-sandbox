package com.github.yuizho.webfluxsandbox.domain

import reactor.core.publisher.Mono

interface QueueOperation {
    fun push(value: Message): Mono<Message>
    fun pop(): Mono<Message>
}


package com.github.yuizho.webfluxsandbox.infra

import com.github.yuizho.webfluxsandbox.domain.Message
import com.github.yuizho.webfluxsandbox.domain.QueueOperation
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
class QueueTemplate : QueueOperation {
    private val queue: Queue<Message> = ArrayDeque()

    override

    fun push(message: Message): Mono<Message> {
        queue.add(message)
        return Mono.just(message)
    }

    override fun pop(): Mono<Message> {
        if (queue.isEmpty()) {
            return Mono.empty()
        }
        return Mono.just(queue.remove())
    }
}
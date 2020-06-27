package com.github.yuizho.webfluxsandbox.controllers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
class DemoControllerTest {
    lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() {
        // https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#webtestclient
        // applicationContextを入れるやり方だとエラーになったのでControllerを指定するやり方で実装
        client = WebTestClient
                .bindToController(DemoController())
                .build()
    }

    @Test
    fun helloTest() {
        client.get().uri("/hello").exchange()
                .expectStatus().isOk
                // https://taro.hatenablog.jp/entry/2018/08/21/095256
                // expectBodyのgenerics板を使わないとisEqualToに型情報が渡らないので、isEqualTo<Nothing>と
                // しないといけない
                .expectBody<String>()
                .isEqualTo("Hello world")
    }
}
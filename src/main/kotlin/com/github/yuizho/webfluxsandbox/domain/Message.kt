package com.github.yuizho.webfluxsandbox.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class Message @JsonCreator constructor(
        @JsonProperty("value") val value: String
)

class ChannelMessage @JsonCreator constructor(
        @JsonProperty("to") val to: String,
        @JsonProperty("value") val value: String
)
package com.github.yuizho.webfluxsandbox.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Message(@field:JsonProperty("value") val value: String)
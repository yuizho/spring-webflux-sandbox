# spring-webflux-sandbox

a sandbox project of Spring WebFlux


## Getting Started
```
$ ./mvnw clean package && docker-compose up --build
```

## Endpoints
### SSE (Server-Sent Events) with Redis Pub/Sub
a post message by `post message endpoint` is pushed to SSE session.

- post message
  - URL: `http://localhost:8080/redis/post`
  - Data: `{"to":"<channel id>","value":"<message>"}`
  - Example: `$ curl localhost:8080/redis/post -H "Content-type: application/json" -d '{"to": "1", "value": "hello!!"}'`
- SSE
  - `localhost:8080/redis/channel/<channel id>`
  - Example: `$ curl -v localhost:8080/redis/channel/1 -H "Accept: text/event-stream"`

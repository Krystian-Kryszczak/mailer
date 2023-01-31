## Application - Mailer Microservice

---
##### Description:

> This microservice is responsible for sending emails to users.
---
##### Programing language: Kotlin
##### Used technologies:

- Framework » [Micronaut (v. 3.7.3)](https://docs.micronaut.io/3.7.3/guide/index.html)


- E-mail API
    - [Javamail Email Micronaut doc](https://micronaut-projects.github.io/micronaut-email/latest/guide/index.html#javamail)
    - [Javamail Email Website](https://javaee.github.io/javamail/)


- Message Broker »  [Apache Kafka](https://kafka.apache.org/)


- Communication » [gRPC](https://grpc.io/)


- Security » [JSON Web Token](https://jwt.io/)


- Containerization » [Docker](https://www.docker.com/)


- Testing
    - [JUnit5](https://junit.org/junit5/)
    - [Mockito](https://site.mockito.org/)
---
> I used Micronaut instead of Spring Framework, because it runs faster and uses less memory. &#9889;<br>
[Micronaut vs Quarkus vs Spring Boot Performance on JDK 14](https://micronaut.io/2020/04/07/micronaut-vs-quarkus-vs-spring-boot-performance-on-jdk-14/)

> For communication between services and clients, I use gRPC with Protocol Buffers,<br>
because it is faster than RESTful API + JSON, but the disadvantage is longer implementation time.<br>
[REST vs gRPC (Performance)](https://medium.com/sahibinden-technology/benchmarking-rest-vs-grpc-5d4b34360911#828a)
---












## Micronaut 3.7.5 Documentation

- [User Guide](https://docs.micronaut.io/3.7.5/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.7.5/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.7.5/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Protobuf Gradle Plugin](https://plugins.gradle.org/plugin/com.google.protobuf)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
## Feature kafka documentation

- [Micronaut Kafka Messaging documentation](https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html)


## Feature security-jwt documentation

- [Micronaut Security JWT documentation](https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html)


## Feature security documentation

- [Micronaut Security documentation](https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html)


## Feature reactor documentation

- [Micronaut Reactor documentation](https://micronaut-projects.github.io/micronaut-reactor/snapshot/guide/index.html)


## Feature test-resources documentation

- [Micronaut Test Resources documentation](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/)


## Feature email-javamail documentation

- [Micronaut Javamail Email documentation](https://micronaut-projects.github.io/micronaut-email/latest/guide/index.html#javamail)

- [https://javaee.github.io/javamail/](https://javaee.github.io/javamail/)



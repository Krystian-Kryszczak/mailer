FROM openjdk:17
RUN set -x \
    mkdir -p /mailer
COPY ./build/libs/mailer-0.4-all.jar /mailer/
EXPOSE $MAILER_GRPC_PORT

ENV MAILER_ADDRESS=socialmedia.app.mailer@gmail.com
ENV MAILER_ADDRESS_PASSWORD=flyedtbyngukhznp
ENV MAILER_HOST="smtp.gmail.com"
ENV MAILER_PORT=465

ENV MAILER_GRPC_PORT=50011

ENV KAFKA_HOST=127.0.0.1
ENV KAFKA_PORT=9092

WORKDIR /mailer
CMD ["java", "-jar", "mailer-0.4-all.jar"]

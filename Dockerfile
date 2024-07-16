FROM maven:3.8.5-openjdk-17

WORKDIR /demo
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run

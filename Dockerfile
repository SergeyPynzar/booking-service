ARG IMAGE_JRE=repository.javaguru.team:5061/bellsoft/liberica-runtime-container:jre-17-slim-musl

FROM ${IMAGE_JRE} AS layers
WORKDIR /layers
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM ${IMAGE_JRE}
WORKDIR /workspace
COPY --from=layers /layers/dependencies/ ./
COPY --from=layers /layers/snapshot-dependencies/ ./
COPY --from=layers /layers/spring-boot-loader/ ./
COPY --from=layers /layers/application/ ./

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
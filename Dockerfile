FROM eclipse-temurin:22-jdk as build
LABEL authors="isa.inalcik"

WORKDIR /build

COPY . /build

RUN --mount=type=cache,target=/root/.m2 ./mvnw clean install -DskipTests

FROM trinodb/trino:447

COPY --from=build /build/target/trino-ai-*/. /usr/lib/trino/plugin/trino-ai/.

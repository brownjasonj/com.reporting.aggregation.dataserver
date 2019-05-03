FROM anapsix/alpine-java:8_jdk_unlimited
ARG JAR_FILE
COPY target/${JAR_FILE} /app/com.reporting.aggregation.dataserver.jar
VOLUME /tmp
ENTRYPOINT ["java","-jar", "/app/com.reporting.aggregation.dataserver.jar"]
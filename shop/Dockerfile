FROM openjdk:8-jre
ADD target/shop-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8010

#Create empty environment variables
ENV SFX_REALM=""
ENV SFX_TOKEN=""
ENV USERNAME=""

#Copy otel javaagent
COPY splunk-otel-javaagent-all.jar .

ENTRYPOINT java -javaagent:splunk-otel-javaagent-all.jar -Dotel.resource.attributes=service.name=shop,environment=${USERNAME}-tagging -Dotel.exporter.jaeger.endpoint=https://ingest.${SFX_REALM:-us2}.signalfx.com/v2/trace -Dsignalfx.auth.token=${SFX_TOKEN} -jar ./app.jar

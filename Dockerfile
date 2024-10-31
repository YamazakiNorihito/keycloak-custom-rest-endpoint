ARG dbflavor=mysql
ARG kcversion=22.0.3

FROM quay.io/keycloak/keycloak:${kcversion} AS builder

ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

ARG dbflavor
ENV KC_DB=${dbflavor}

COPY cache-ispn-jdbc-ping-${dbflavor}.xml /opt/keycloak/conf/cache-ispn-jdbc-ping.xml

# カスタムプロバイダ(JAR)を追加
COPY custom-rest-api/target/custom-rest-api-1.0-SNAPSHOT.jar /opt/keycloak/providers/

# RUN /opt/keycloak/bin/kc.sh build --http-relative-path=/auth
RUN /opt/keycloak/bin/kc.sh build --cache-config-file=cache-ispn-jdbc-ping.xml --http-relative-path=/auth

FROM quay.io/keycloak/keycloak:${kcversion}
COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
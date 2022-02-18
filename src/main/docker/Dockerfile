FROM java:8
VOLUME /tmp
ADD fbyx.jar fbyx.jar
ENTRYPOINT ["java","-Dlog4j2.formatMsgNoLookups=true","-Djava.security.egd=file:/dev/./urandom","-jar","/fbyx.jar","--spring.profiles.active=prod"]
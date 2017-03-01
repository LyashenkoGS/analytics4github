FROM strictlybusiness/oracle-java8
VOLUME /tmp
#copy the project files
ADD token.txt /var/token.txt
ADD gradle /gradle
ADD gradlew /gradlew
ADD src /src
ADD build.gradle /build.gradle
ENV JAVA_OPTS=""
#build the project
RUN sh -c './gradlew build -x test'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /build/libs/analytics4github-0.0.1-SNAPSHOT.jar" ]
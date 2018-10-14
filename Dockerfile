FROM openjdk:9
ADD target/git-viewer.jar git-viewer.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "git-viewer.jar"]
CMD ["--spring.config.location=file:./application.properties"]
COPY application.properties .

#docker build -f Dockerfile -t git-viewer .
#docker run -p 8080:8080 git-viewer
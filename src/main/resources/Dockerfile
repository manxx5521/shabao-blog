FROM mykro/java8-jre:latest
MAINTAINER xiaoshabao
VOLUME /tmp

#安装maven
RUN wget http://apache-mirror.rbc.ru/pub/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
RUN tar xzvf apache-maven-3.3.9-bin.tar.gz
RUN cp -R apache-maven-3.3.9 /usr/local/bin
RUN rm -rf apache-maven-3.3.9-bin.tar.gz
RUN export PATH=apache-maven-3.3.9/bin:$PATH
RUN export PATH=/usr/local/bin/apache-maven-3.3.9/bin:$PATH
RUN ln -s /usr/local/bin/apache-maven-3.3.9/bin/mvn /usr/local/bin/mvn
RUN echo $PATH

RUN apt-get update && apt-get install -y zip
RUN wget https://codeload.github.com/manxx5521/shabao-base/zip/master
RUN unzip master
RUN mv shabao-base-master shabao-base
RUN cp -R shabao-base /app/
RUN rm -rf master
RUN rm -rf shabao-base

RUN wget https://codeload.github.com/manxx5521/shabao-blog/zip/master
RUN unzip master
RUN mv shabao-blog-master shabao-blog
RUN cp -R shabao-blog /app/
RUN rm -rf master
RUN rm -rf shabao-blog


WORKDIR /app/shabao-base/ 
RUN mvn install
WORKDIR /app/shabao-blog/ 
RUN mvn package
EXPOSE  8080  
#指定容器的启动，可以指定参数
#CMD ["java","-jar","target/shabao-blog-0.0.1.jar"]
#容器启动时执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","target/shabao-blog-0.0.1.jar"]
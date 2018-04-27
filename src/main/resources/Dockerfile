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

RUN wget -O shabao-base-master.zip https://codeload.github.com/manxx5521/shabao-base/zip/master
RUN mv shabao-base-master.zip /app/
RUN wget -O shabao-blog-master.zip https://codeload.github.com/manxx5521/shabao-blog/zip/master
RUN mv shabao-blog-master.zip /app/

WORKDIR /app/
RUN unzip shabao-base-master.zip && rm -rf shabao-base-master.zip
RUN unzip shabao-blog-master.zip && rm -rf shabao-blog-master.zip
RUN cd /app/shabao-base-master && mvn install
RUN cd /app/shabao-blog-master && mvn package

#删除多余的maven依赖
RUN rm -rf /root/.m2
#容器启动时执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/shabao-blog-master/target/shabao-blog-0.0.1.jar"]

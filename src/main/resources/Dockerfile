#********************************
# 博客系统构建：版本1.0.3
# 下载 docker pull registry-vpc.cn-hongkong.aliyuncs.com/xiaoshabao/shabao-blog
# 启动 docker run -p 80:80 -p 443:443 --name blog -v /usr/docker/blog:/app/data -d registry-vpc.cn-hongkong.aliyuncs.com/xiaoshabao/shabao-blog
# 重启和停止 docker restart blog 和 docker stop blog
# 删除容器和镜像 docker rm blog 和 docker rmi registry-vpc.cn-hongkong.aliyuncs.com/xiaoshabao/shabao-blog
# 进入容器 docker exec -it blog bash
#********************************
FROM mykro/java8-jre:latest
MAINTAINER manxx
VOLUME /tmp

#安装maven
ARG MAVEN_VERSION=3.5.4
RUN wget -O apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz
RUN tar xzvf apache-maven.tar.gz && rm -rf apache-maven.tar.gz
RUN cp -R apache-maven-${MAVEN_VERSION} /usr/local/bin && rm -rf apache-maven-${MAVEN_VERSION}
RUN export PATH=apache-maven-${MAVEN_VERSION}/bin:$PATH
RUN export PATH=/usr/local/bin/apache-maven-${MAVEN_VERSION}/bin:$PATH
RUN ln -s /usr/local/bin/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/local/bin/mvn
RUN echo $PATH

RUN apt-get update && apt-get install -y zip
WORKDIR /app/
RUN wget -O shabao-base-master.zip https://codeload.github.com/manxx5521/shabao-base/zip/master
RUN unzip shabao-base-master.zip && rm -rf shabao-base-master.zip
RUN cd shabao-base-master && mvn install && cd .. && rm -rf shabao-base-master

RUN wget -O shabao-blog-master.zip https://codeload.github.com/manxx5521/shabao-blog/zip/master
RUN unzip shabao-blog-master.zip && rm -rf shabao-blog-master.zip
RUN cd shabao-blog-master && mvn package && mv target/shabao-blog-0.0.1.jar /app/ && rm -rf /app/shabao-blog-master
RUN cd /app/ && mkdir data && cd data && mkdir upload && mkdir logs && mkdir indexs
 
WORKDIR /app/
#删除多余的maven依赖
RUN rm -rf /root/.m2 && rm -rf /usr/local/bin/apache-maven-3.5.3 && rm -rf /var/lib/apt/lists/*
#容器启动时执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","shabao-blog-0.0.1.jar","--spring.profiles.active=prd"]

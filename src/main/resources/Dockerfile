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
#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#安装maven
ARG MAVEN_VERSION=3.5.4 && echo '开始安装maven...'
RUN wget -q -O apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz
RUN tar -xzf apache-maven.tar.gz && rm -rf apache-maven.tar.gz
RUN cp -R apache-maven-${MAVEN_VERSION} /usr/local/bin && rm -rf apache-maven-${MAVEN_VERSION}
RUN export PATH=apache-maven-${MAVEN_VERSION}/bin:$PATH
RUN export PATH=/usr/local/bin/apache-maven-${MAVEN_VERSION}/bin:$PATH
RUN ln -s /usr/local/bin/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/local/bin/mvn
RUN echo $PATH && echo '完成安装maven'

RUN apt-get update && apt-get install -y zip
WORKDIR /app/ && echo '开始安装工程...'
RUN wget -q -O shabao-base-parent.zip https://codeload.github.com/manxx5521/shabao-base-parent/zip/master && echo 'shabao-base-parent 下载完成'
RUN unzip -q shabao-base-parent.zip && rm -rf shabao-base-parent.zip && echo 'shabao-base-parent 解压完成'
RUN cd shabao-base-parent && mvn install && cd .. && rm -rf shabao-base-parent && echo 'shabao-base-parent 安装完成'

RUN wget -q -O shabao-base-master.zip https://codeload.github.com/manxx5521/shabao-base/zip/master && echo 'shabao-base 下载完成'
RUN unzip -q shabao-base-master.zip && rm -rf shabao-base-master.zip && echo 'shabao-base 解压完成'
RUN cd shabao-base-master && mvn install && cd .. && rm -rf shabao-base-master && echo 'shabao-base 安装完成'

RUN wget -q -O shabao-blog-master.zip https://codeload.github.com/manxx5521/shabao-blog/zip/master && echo 'shabao-blog 下载完成'
RUN unzip -q shabao-blog-master.zip && rm -rf shabao-blog-master.zip && echo 'shabao-blog 解压完成'
RUN cd shabao-blog-master && mvn package && mv target/shabao-blog-0.0.1.jar /app/ && rm -rf /app/shabao-blog-master
RUN cd /app/ && mkdir data && cd data && mkdir upload && mkdir logs && mkdir indexs && echo 'shabao-blog 安装完成'
 
WORKDIR /app/
#删除多余的maven依赖
RUN rm -rf /root/.m2 && rm -rf /usr/local/bin/apache-maven-3.5.3 && rm -rf /var/lib/apt/lists/*
#容器启动时执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","shabao-blog-0.0.1.jar","--spring.profiles.active=prd"]

#********************************
# 博客系统构建：版本1.0.0
# 下载 docker pull registry-vpc.cn-hongkong.aliyuncs.com/xiaoshabao/shabao-blog-web:1.0.0
# 启动 docker run -p 80:80 -p 443:443 --name blog -v /usr/docker/blog:/app/data -d registry-vpc.cn-hongkong.aliyuncs.com/xiaoshabao/shabao-blog-web:1.0.0
# 重启和停止 docker restart blog 和 docker stop blog
# 删除容器和镜像 docker rm blog 和 docker rmi registry-vpc.cn-hongkong.aliyuncs.com/xiaoshabao/shabao-blog-web:1.0.0
# 进入容器 docker exec -it blog bash
#********************************
FROM mykro/java8-jre:latest
MAINTAINER manxx
VOLUME /tmp
#设置版本
ARG MAVEN_VERSION=3.5.4
ARG SHABAO_BASE_VERSION=1.0.0
ARG SHABAO_BLOG_VERSION=1.0.0

#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

RUN echo '开始安装maven...'
RUN wget -q -O apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz
RUN tar -xzf apache-maven.tar.gz && rm -rf apache-maven.tar.gz
RUN cp -R apache-maven-${MAVEN_VERSION} /usr/local/bin && rm -rf apache-maven-${MAVEN_VERSION}
RUN export PATH=apache-maven-${MAVEN_VERSION}/bin:$PATH
RUN export PATH=/usr/local/bin/apache-maven-${MAVEN_VERSION}/bin:$PATH
RUN ln -s /usr/local/bin/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/local/bin/mvn
RUN echo $PATH && echo '完成安装maven'

RUN apt-get update && apt-get install -y zip
WORKDIR /app/

#安装项目基础依赖
RUN wget -q -O shabao-base-${SHABAO_BASE_VERSION}.zip https://codeload.github.com/manxx5521/shabao-base/zip/${SHABAO_BASE_VERSION}
RUN unzip -q shabao-base-${SHABAO_BASE_VERSION}.zip && rm -rf shabao-base-${SHABAO_BASE_VERSION}.zip
RUN cd shabao-base-${SHABAO_BASE_VERSION} && mvn install -q && cd .. && rm -rf shabao-base-${SHABAO_BASE_VERSION} && echo 'shabao-base 安装完成'

#安装博客项目
RUN wget -q -O shabao-blog-${SHABAO_BLOG_VERSION}.zip https://codeload.github.com/manxx5521/shabao-blog/zip/${SHABAO_BLOG_VERSION}
RUN unzip -q shabao-blog-${SHABAO_BLOG_VERSION}.zip && rm -rf shabao-blog-${SHABAO_BLOG_VERSION}.zip
RUN cd shabao-blog-${SHABAO_BLOG_VERSION} && mvn install -q
RUN cd /app/shabao-blog-${SHABAO_BLOG_VERSION}/shabao-blog-core && mvn install -q
RUN cp /app/shabao-blog-${SHABAO_BLOG_VERSION}/shabao-blog-shiro/target/shabao-blog-shiro-${SHABAO_BLOG_VERSION}.jar /app/shabao-blog-shiro.jar
RUN rm -rf /app/shabao-blog-${SHABAO_BLOG_VERSION}
RUN cd /app/ && mkdir data && cd data && mkdir upload && mkdir logs && mkdir indexs && echo 'shabao-blog 安装完成'
 
WORKDIR /app/
#删除多余的maven依赖
RUN rm -rf /root/.m2 && rm -rf /usr/local/bin/${MAVEN_VERSION} && rm -rf /var/lib/apt/lists/*
#容器启动时执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","shabao-blog-shiro.jar","--spring.profiles.active=prd"]

FROM hseeberger/scala-sbt:8u222_1.3.2_2.12.10
WORKDIR /app
COPY . /app
ENTRYPOINT ["sh", "./entrypoint.sh"]
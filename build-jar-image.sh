# mvn clean package

docker build -t jaiminho  .

docker tag jaiminho boaglio/jaiminho

docker push boaglio/jaiminho:latest


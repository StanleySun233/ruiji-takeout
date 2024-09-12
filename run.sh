docker build -t reggie:latest .
docker run -d -p 8081:8080 --name reggie reggie:latest
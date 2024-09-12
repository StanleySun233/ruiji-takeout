docker build -t your-app:latest .
docker run -d -p 8081:8080 --name my-app your-app:latest
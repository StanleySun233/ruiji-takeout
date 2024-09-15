#!/bin/bash

docker build -t ruiji-takeout .
docker run -d --restart=always --name ruiji -p 8081:8080 ruiji-takeout
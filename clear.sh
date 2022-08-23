#!/bin/sh
CONTAINER_IDS=`docker ps -a | grep Exited | awk '{print \$1}'`
if [ ! -n "$CONTAINER_IDS" ]; then
  echo "there are no Exited container...skip"
else
  `docker rm $CONTAINER_IDS`
fi

IMAGE_IDS=`docker images | grep '<none>' | awk '{print \$3}'`
if [ ! -n "$IMAGE_IDS" ]; then
  echo "there are no <none> image...skip"
else
  `docker rmi $IMAGE_IDS`
  exit 0
fi

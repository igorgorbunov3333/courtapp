#build executable jar
(cd ../courthubmanagement && mvn package spring-boot:repackage -e)

#remove stopped containers
sudo docker-compose rm

#build docker image
(cd ../courthubmanagement && docker build . --tag ihorbunov/courthubmanagement:0.1.0)

#delete all untagged images
sudo docker rmi "$(sudo docker images -f"dangling=true" -q)"

#stop postgresql service on host server
#systemctl stop postgresql

#rise up 'courthubmanagement' and 'postgresql' container
#docker-compose up -d
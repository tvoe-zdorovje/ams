# ams - WIP

Run the project by Docker Compose
- `docker compose up`

Stop all containers and remove all images and volumes
- `.reset_docker.sh`

Run the project by k8s
- `./infrastructure/k8s/start-cluster.sh`

Stop k8s cluster removing all namespaces
- `./infrastructure/k8s/stop-cluster.sh`

Use `--delete` flag to delete the cluster
- `./infrastructure/k8s/stop-cluster.sh --delete`


Build docker images
- `./build-docker-images.sh --liquibase --latest --push`
- `--liquibase` flag includes liquibase images
- `--latest` flag tags images as latest
- `--push` pushes images to the remote repository
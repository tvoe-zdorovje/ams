sudo docker compose down
#sudo docker image rm ams-user_db
sudo docker image rm ams-liquibase_user_db
sudo docker volume rm ams_user_db_volume
sudo docker builder prune -af

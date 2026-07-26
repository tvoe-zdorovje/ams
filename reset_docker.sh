docker compose down

docker volume rm        ams_administration_db_volume \
                        ams_brand_db_volume \
                        ams_studio_db_volume \
                        ams_user_db_volume \
                        ams_kafka-volume \
#docker builder prune -af

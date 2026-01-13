docker compose down
docker image rm    ams-gateway \
                        ams-user-service \
                        ams-auth-service
docker image rm    ams-user_db \
                        ams-brand_db \
                        ams-studio_db \
                        ams-appointment_db \
                        ams-administration_db
docker image rm    ams-liquibase_user_db \
                        ams-liquibase_brand_db \
                        ams-liquibase_studio_db \
                        ams-liquibase_appointment_db \
                        ams-liquibase_administration_db
docker volume rm   ams_user_db_volume \
                        ams_brand_db_volume \
                        ams_studio_db_volume \
                        ams_administration_db_volume
#docker builder prune -af

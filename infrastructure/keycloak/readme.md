```docker exec -it keycloak bash```

```./opt/keycloak/bin/kc.sh export --dir /tmp/export --users realm_file```

```docker cp keycloak:/tmp/export/ams-realm.json ./infrastructure/keycloak/realms```
FROM liquibase/liquibase:latest-alpine

USER root
COPY install_pg_prove.sh run_tests.sh /liquibase/
WORKDIR /liquibase/
RUN chmod +x \
    ./install_pg_prove.sh \
    ./run_tests.sh && \
    ./install_pg_prove.sh

CMD ["sh", "-xc", "liquibase update && ./run_tests.sh"]

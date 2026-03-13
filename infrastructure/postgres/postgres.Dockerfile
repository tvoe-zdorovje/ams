FROM postgres:17.2-alpine3.21

COPY install_pgtap.sh /
RUN chmod +x /install_pgtap.sh && \
    /install_pgtap.sh

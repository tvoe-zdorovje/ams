CREATE PUBLICATION user_outbox_publication
    FOR TABLE users."user", debezium.heartbeat_table
    WITH (publish = 'insert');

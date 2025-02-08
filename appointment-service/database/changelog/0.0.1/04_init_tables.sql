CREATE TYPE appointments.appointment_status AS ENUM (
    'REQUESTED',    -- requested or corrected by a client
    'CORRECTED',    -- corrected by authorized staff
    'APPROVED',     -- approved by authorized staff
    'COMPLETED',    -- completed
    'CANCELLED'     -- cancelled by a client or authorized staff
);

CREATE TABLE IF NOT EXISTS appointments.appointment(
    id UUID PRIMARY KEY NOT NULL,
    description VARCHAR(255) NOT NULL,
    client_user_id INT, -- TODO client id != master id \ manager id
    master_user_id INT,
    manager_user_id INT,
    studio_id INT,
    status appointments.appointment_status NOT NULL DEFAULT 'REQUESTED',
    comment VARCHAR(255)
);

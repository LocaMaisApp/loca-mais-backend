CREATE SCHEMA IF NOT EXISTS locamais;

SET search_path TO locamais;

DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS contracts CASCADE;
DROP TABLE IF EXISTS maintenances CASCADE;
DROP TABLE IF EXISTS tickets CASCADE;
DROP TABLE IF EXISTS advertisement_images CASCADE;
DROP TABLE IF EXISTS advertisements CASCADE;
DROP TABLE IF EXISTS properties CASCADE;
DROP TABLE IF EXISTS landlords CASCADE;
DROP TABLE IF EXISTS tenants CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS ticket_status_enum CASCADE;
CREATE TYPE ticket_status_enum AS ENUM ('FINISHED', 'PROGRESS', 'PENDENT');
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    last_name VARCHAR(45) NOT NULL,
    cpf VARCHAR(45) UNIQUE NOT NULL,
    phone VARCHAR(45),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS landlords (
                                         user_id INT PRIMARY KEY,
                                         CONSTRAINT fk_landlord_user1
                                         FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS properties (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    name VARCHAR(255),
    street VARCHAR(255),
    size INT,
    bathroom_quantity INT,
    state VARCHAR(45),
    suites INT,
    car_space INT,
    complement VARCHAR(255),
    number INT,
    room_quantity INT,
    city VARCHAR(45),
    landlord_id INT NOT NULL,
    CONSTRAINT fk_property_landlord1
    FOREIGN KEY (landlord_id)
    REFERENCES landlords (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE UNIQUE INDEX IF NOT EXISTS unique_house_address
    ON properties (street, number, city, state)
    WHERE complement IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS unique_condo_address
    ON properties (street, number, city, state, complement)
    WHERE complement IS NOT NULL;


CREATE TABLE IF NOT EXISTS advertisements (
                                              id SERIAL PRIMARY KEY,
                                              created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                              updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                              active BOOLEAN DEFAULT TRUE,
                                              name VARCHAR(255),
    condominium_value NUMERIC(10, 2),
    description VARCHAR(255),
    value NUMERIC(10, 2),
    iptu_value NUMERIC(10, 2),
    property_id INT NOT NULL UNIQUE,
    CONSTRAINT fk_advertisement_property1
    FOREIGN KEY (property_id)
    REFERENCES properties (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE TABLE IF NOT EXISTS advertisement_images (
                                                    url VARCHAR(255) NOT NULL,
    advertisement_id INT NOT NULL,
    PRIMARY KEY (advertisement_id, url),
    CONSTRAINT fk_advertisement_images_advertisement
    FOREIGN KEY (advertisement_id)
    REFERENCES advertisements (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS tenants (
                                       user_id INT PRIMARY KEY,
                                       CONSTRAINT fk_tenant_user1
                                       FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );


CREATE TABLE IF NOT EXISTS tickets (
                                       id SERIAL PRIMARY KEY,
                                       urgent BOOLEAN NOT NULL,
                                       description VARCHAR(255) NOT NULL,
    status ticket_status_enum NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    property_id INT NOT NULL,
    tenant_id INT NOT NULL,
    CONSTRAINT fk_ticket_property1
    FOREIGN KEY (property_id)
    REFERENCES properties (id)
                         ON DELETE NO ACTION
                         ON UPDATE NO ACTION,
    CONSTRAINT fk_ticket_tenant1
    FOREIGN KEY (tenant_id)
    REFERENCES tenants (user_id)
                         ON DELETE NO ACTION
                         ON UPDATE NO ACTION
    );

CREATE TABLE IF NOT EXISTS maintenances (
                                            id SERIAL PRIMARY KEY,
                                            created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                            updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                            finished_at TIMESTAMP WITHOUT TIME ZONE,
                                            total_value NUMERIC(10, 2),
    ticket_id INT NOT NULL,
    CONSTRAINT fk_maintenance_ticket1
    FOREIGN KEY (ticket_id)
    REFERENCES tickets (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE TABLE IF NOT EXISTS contracts (
                                         id SERIAL PRIMARY KEY,
                                         created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                         payment_day INT,
                                         monthly_value NUMERIC(10, 2),
    duration INT,
    deposit NUMERIC(10, 2),
    property_id INT NOT NULL,
    tenant_id INT NOT NULL,
    CONSTRAINT fk_contract_property1
    FOREIGN KEY (property_id)
    REFERENCES properties (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_contract_tenant1
    FOREIGN KEY (tenant_id)
    REFERENCES tenants (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE TABLE IF NOT EXISTS payments (
                                        id SERIAL PRIMARY KEY,
                                        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                        value NUMERIC(10, 2),
    tax NUMERIC(10, 2),
    contract_id INT NOT NULL,
    CONSTRAINT fk_payment_contract1
    FOREIGN KEY (contract_id)
    REFERENCES contracts (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );
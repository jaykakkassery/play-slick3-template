-- User schema

-- !Ups

create table example.address (
                            `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `addressline` TEXT NOT NULL,
                            `employeeid` BIGINT NOT NULL,
                            FOREIGN KEY (employeeid)
                                REFERENCES employee(id)
                                ON DELETE CASCADE
);

-- !Downs
drop table example.address
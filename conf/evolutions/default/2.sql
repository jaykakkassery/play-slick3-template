-- User schema

-- !Ups

create table example.employee (
                        `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `name` TEXT NOT NULL
)

-- !Downs
drop table example.employee
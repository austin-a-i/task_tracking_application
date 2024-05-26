DROP TABLE IF EXISTS user_authority;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS app_user;
DROP TABLE IF EXISTS authorities;


create table app_user (
        id bigint not null AUTO_INCREMENT,
        username varchar(255) not null unique,
        email varchar(255) not null,
        password varchar(255) not null,
        enabled boolean not null,
        primary key (id)
        );

create table authorities (
        id bigint not null AUTO_INCREMENT,
        name varchar(255) not null unique,
        primary key (id)
        );

create table task (
        id bigint not null AUTO_INCREMENT,
        task varchar(255),
        description varchar(255),
        due_date date,
        status varchar(255) check (status in ('IN_PROGRESS','EXTENDED','DUE','COMPLETED')),
        user_id bigint,
        primary key (id),
        unique (task, user_id)
        );

create table user_authority (
         user_id bigint not null,
         authority_id bigint not null
        );

    alter table task
    add constraint FKa21ft97nj7thwrp5d31xdaxr
    foreign key (user_id)
    references app_user (id);

    alter table user_authority
    add constraint FKnjgq9pgqd4suu6fi9lk0wywop
    foreign key (user_id)
    references app_user (id);

    alter table user_authority
    add constraint FKqy5hbpnswue8ys7ohmufg094k
    foreign key (authority_id)
    references authorities (id);


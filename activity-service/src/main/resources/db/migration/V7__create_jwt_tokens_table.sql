CREATE TABLE jwt_tokens
(
    id bigint generated by default as identity,
    token   text unique,
    expired boolean not null,
    user_id bigint,
    primary key (id)
);

alter table if exists jwt_tokens
    add constraint jwt_token_user_fk
        foreign key (user_id) references users;
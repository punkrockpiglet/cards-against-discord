drop table if exists white_cards;
create table white_cards(
    id   int primary key not null,
    text text            not null unique
);
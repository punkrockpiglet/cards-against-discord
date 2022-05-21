drop table if exists black_cards;
create table black_cards(
    id     int primary key not null,
    blanks int             not null check(blanks > 0),
    text   text            not null unique
);
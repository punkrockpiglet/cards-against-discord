drop table if exists player_cards cascade;
drop table if exists selected_cards cascade;

create table player_cards
(
    player_id     int not null,
    white_card_id int not null,

    primary key (player_id, white_card_id)
);

alter table player_cards
    add constraint fk_player_id foreign key (player_id) references players (id);

alter table player_cards
    add constraint fk_white_card foreign key (white_card_id) references white_cards (id);


create table selected_cards
(
    player_id int not null,
    white_card_id int not null,

    primary key (player_id, white_card_id)
);
alter table selected_cards
    add constraint fk_player_id foreign key (player_id) references players (id);

alter table selected_cards
    add constraint fk_white_card foreign key (white_card_id) references white_cards (id);

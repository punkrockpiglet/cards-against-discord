drop table if exists lobbies;
create table lobbies(
    id         int      primary key not null,
    owner      bigint               not null,
    players    bigint[]             not null,
    guild_id   bigint               not null check(guild_id > 0),
    channel_id bigint               not null check(channel_id > 0),
    message_id bigint               not null check(message_id > 0)
);
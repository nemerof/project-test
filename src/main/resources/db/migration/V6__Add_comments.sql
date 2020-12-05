create table comment (
                         id int8 not null,
                         filename varchar(255),
                         post_time timestamp,
                         text varchar(2048) not null,
                         user_id int8,
                         primary key (id)
);

create table message_comments (
                                  message_id bigint not null references message,
                                  comments_id bigint not null references comment,
                                  primary key (message_id, comments_id)
)
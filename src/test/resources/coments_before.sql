delete from comment;
delete from message_comments;

insert into message (id, filename, post_time, text, user_id)
values (1, 'admin_test_message.png', '2020-12-12', 'Admin comment', '1'),
       (2, 'user_test_message.png', '2020-12-12', 'User comment', '2');

insert into message_comments (message_id, comments_id)
values (1, 1),
       (2, 2);
delete from comment;
delete from message_comments;

insert into comment (id, filename, post_time, text, user_id)
values (3, 'admin_test_message.png', '2020-12-12 15:45', 'Admin comment', '1'),
       (4, 'user_test_message.png', '2020-12-12 16:12', 'User comment', '2');

insert into message_comments (message_id, comments_id)
values (1, 3),
       (2, 4);
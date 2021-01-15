delete from comment;
delete from message_comments;

insert into comment (id, filename, post_time, text, user_id)
values (1005, 'admin_test_message.png', '2020-12-12 15:45', 'Admin comment', '1001'),
       (1006, 'user_test_message.png', '2020-12-12 16:12', 'User comment', '1002');

insert into message_comments (message_id, comments_id)
values (1003, 1005),
       (1004, 1006);
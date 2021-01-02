delete from comment;
delete from message_comments;

insert into comment (id, filename, post_time, text, user_id)
values (100003, 'admin_test_message.png', '2020-12-12 15:45', 'Admin comment', '1'),
       (100004, 'user_test_message.png', '2020-12-12 16:12', 'User comment', '2');

insert into message_comments (message_id, comments_id)
values (100001, 100003),
       (100002, 100004);
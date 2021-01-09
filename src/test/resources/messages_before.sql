delete from message_likes;
delete from message_comments;
delete from comment;
delete from message;

insert into message (id, filename, post_time, text, user_id)
values (1003, 'admin_test_message.png', '2020-12-12 14:01', 'Admin message', '1001'),
       (1004, 'user_test_message.png', '2020-12-12 14:03', 'User message', '1002');
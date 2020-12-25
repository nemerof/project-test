delete from message;

insert into message (id, filename, post_time, text, user_id)
values (1, 'admin_test_message.png', '2020-12-12', 'Admin message', '1'),
       (2, 'user_test_message.png', '2020-12-12', 'User message', '2');
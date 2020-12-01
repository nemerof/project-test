insert into usr (id, username, password, active, email, profile_pic)
values (1, 'admin', 'admin123', true, 'admin123@gmail.com', 'default-profile-icon.png');

insert into user_role (user_id, roles)
values (1, 'USER'), (1, 'ADMIN');
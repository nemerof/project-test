delete from user_role;
delete from usr;

insert into usr (id, username, password, active, email, profile_pic, real_name, date_of_birth, city)
values (1000, 'usr', '$2a$08$3OVVF0fxn/zHgUOcOXalL.7FfHD7/xBsvOFs3bjGz9v3NV.bJYQiC',
        true, 'usr123@gmail.com', 'default-profile-icon.png', 'Great Name', '1993-11-01', 'Moscow'),
       (1001, 'admin', '$2a$08$VE4y9k.Chi.QmWWDVBVEE.o3VO2qJddfbUrtDygeXywvDZaBfd4ce',
        true, 'admin123@gmail.com', 'default-profile-icon.png', 'Artyom Kosenko', '1990-10-12', 'Penza'),
       (1002, 'user', '$2a$08$3OVVF0fxn/zHgUOcOXalL.7FfHD7/xBsvOFs3bjGz9v3NV.bJYQiC',
        true, 'user123@gmail.com', 'default-profile-icon.png', 'Pasha Alekseev', '1999-02-23', 'Cheboksary');

insert into user_role (user_id, roles)
values (1001, 'USER'), (1001, 'ADMIN');
insert into user_role (user_id, roles)
values (1000, 'USER'), (1002, 'USER');

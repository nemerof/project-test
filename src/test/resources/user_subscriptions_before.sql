delete from user_subscriptions;

insert into user_subscriptions (channel_id, subscriber_id)
values (1001, 1002),
       (1002, 1001);
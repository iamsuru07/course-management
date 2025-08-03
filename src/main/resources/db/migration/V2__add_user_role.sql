alter table user_account
add column role varchar(5) not null default 'USER' after user_id;
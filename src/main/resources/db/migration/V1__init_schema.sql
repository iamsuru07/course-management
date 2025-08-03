Create table user_account (
    user_id int auto_increment primary key,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    email varchar(50) not null unique,
    password varchar(255) not null,
    created_at datetime,
    updated_at datetime
);

Create table course (
    id int auto_increment primary key,
    name varchar(20) not null,
    createdBy varchar(20) not null,
    description text,
    price int,
    duration int not null unique,
    created_at datetime,
    updated_at datetime
);

Create table purchased_course (
    id int auto_increment primary key,
    user_id int not null,
    course_id int not null,
    purchased_date datetime not null,
    expiring_date datetime not null
);
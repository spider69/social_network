create table Users(
    id varchar(100) not null PRIMARY KEY,
    name varchar(100) not null,
    password_hash varchar(255) not null,
    salt binary(64) not null
);

create table Sessions(
    id varchar(100) not null,
    user_id varchar(100) not null,

    FOREIGN KEY (user_id) REFERENCES Users (id) ON UPDATE NO ACTION ON DELETE CASCADE
);

create table UserForms(
    id varchar(100) not null PRIMARY KEY,
    first_name varchar(100),
    last_name varchar(100),
    age integer,
    gender varchar(1),
    interests varchar(255),
    city varchar(100),

    FOREIGN KEY (id) REFERENCES Users (id) ON UPDATE NO ACTION ON DELETE CASCADE
);

create table Friends(
    user_id varchar(100) not null,
    friend_id varchar(100) not null,

    FOREIGN KEY (user_id) REFERENCES Users (id) ON UPDATE NO ACTION ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES Users (id) ON UPDATE NO ACTION ON DELETE CASCADE,
    PRIMARY KEY(user_id, friend_id)
);
create table Users(
    id varchar(100) not null PRIMARY KEY,
    name varchar(100) not null,
    password varchar(100) not null
);

create table Sessions(
    id varchar(100) not null,
    user_id varchar(100) not null,

    FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);
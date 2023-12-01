create table `user`
(
    `id`       bigint             not null auto_increment primary key,
    `email`    varchar(64) unique not null,
    `password` varchar(128)       not null
);

insert into `user` (`email`, `password`) values ('alice@example.com', 'test');
insert into `user` (`email`, `password`) values ('bob@example.com', 'test');
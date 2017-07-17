create table `category` (
    `id` bigint not null auto_increment,
    `name` varchar(255) not null,
    primary key (`id`)
);
create table `journal` (
    `id` bigint not null auto_increment,
    `name` varchar(255) not null,
    `publish_date` datetime not null,
    `uuid` varchar(255) not null,
    `category_id` bigint not null,
    `publisher_id` bigint not null,
    primary key (`id`)
);
create table `publisher` (
    `id` bigint not null auto_increment,
    `name` varchar(255) not null,
    `user_id` bigint not null,
    primary key (`id`)
);
create table `subscription` (
    `id` bigint not null auto_increment,
    `date` datetime not null,
    `category_id` bigint not null,
    `user_id` bigint not null,
    primary key (`id`)
);
create table `user` (
    `id` bigint not null auto_increment,
    `email` varchar(255) not null,
    `enabled` bit not null,
    `login_name` varchar(255) not null,
    `pwd` varchar(255) not null,
    `role` varchar(255) not null,
    primary key (`id`)
);
alter table `publisher` 
    add constraint UK_ml1xc0aovqkkm2p1lssgjkfas unique (`user_id`);
alter table `journal` 
    add constraint `FKo7apvw5x09vsccnsa7tgctt5v` 
    foreign key (`category_id`) 
    references `category` (`id`);
alter table `journal` 
    add constraint `FKmhefqtb5wp9l7md6id5thf8sn` 
    foreign key (`publisher_id`) 
    references `publisher` (`id`);
alter table `publisher` 
    add constraint `FKnu5vcylym29ffbhjrpvhn20ur` 
    foreign key (`user_id`) 
    references `user` (`id`);
alter table `subscription` 
    add constraint `FKs4sa1awj23bvd5ced9bjlest3` 
    foreign key (`category_id`) 
    references `category` (`id`);
alter table `subscription` 
    add constraint `FKbf8h5kfsyuiwhvowaq1tpkglr` 
    foreign key (`user_id`) 
    references `user` (`id`);

/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/9/26 ÐÇÆÚÈý ÏÂÎç 3:44:11                     */
/*==============================================================*/


drop table if exists menu;

drop table if exists perms;

drop table if exists roles;

drop table if exists roles_perms_relation;

drop table if exists users;

drop table if exists users_menu_relation;

drop table if exists users_roles_relation;

/*==============================================================*/
/* Table: menu                                                  */
/*==============================================================*/
create table menu
(
   menuid               int not null,
   menuName             varchar(20) not null,
   menuUrl              varchar(200) not null,
   menuFilter           varchar(200) not null,
   primary key (menuid)
);

/*==============================================================*/
/* Table: perms                                                 */
/*==============================================================*/
create table perms
(
   permid               int not null,
   permName             varchar(20) not null,
   permTag              varchar(20) not null,
   primary key (permid)
);

/*==============================================================*/
/* Table: roles                                                 */
/*==============================================================*/
create table roles
(
   roleid               int not null,
   roleName             varchar(20) not null,
   primary key (roleid)
);

/*==============================================================*/
/* Table: roles_perms_relation                                  */
/*==============================================================*/
create table roles_perms_relation
(
   roleid               int not null,
   permid               int not null,
   primary key (roleid, permid)
);

/*==============================================================*/
/* Table: users                                                 */
/*==============================================================*/
create table users
(
   userid               int not null,
   userName             varchar(20) not null,
   password             varchar(20) not null,
   email                varchar(50),
   primary key (userid)
);

/*==============================================================*/
/* Table: users_menu_relation                                   */
/*==============================================================*/
create table users_menu_relation
(
   userid               int not null,
   menuid               int not null,
   primary key (userid, menuid)
);

/*==============================================================*/
/* Table: users_roles_relation                                  */
/*==============================================================*/
create table users_roles_relation
(
   userid               int not null,
   roleid               int not null,
   primary key (userid, roleid)
);

alter table roles_perms_relation add constraint FK_roles_perms_relation foreign key (roleid)
      references roles (roleid) on delete restrict on update restrict;

alter table roles_perms_relation add constraint FK_roles_perms_relation2 foreign key (permid)
      references perms (permid) on delete restrict on update restrict;

alter table users_menu_relation add constraint FK_users_menu_relation foreign key (userid)
      references users (userid) on delete restrict on update restrict;

alter table users_menu_relation add constraint FK_users_menu_relation2 foreign key (menuid)
      references menu (menuid) on delete restrict on update restrict;

alter table users_roles_relation add constraint FK_users_roles_relation foreign key (userid)
      references users (userid) on delete restrict on update restrict;

alter table users_roles_relation add constraint FK_users_roles_relation2 foreign key (roleid)
      references roles (roleid) on delete restrict on update restrict;


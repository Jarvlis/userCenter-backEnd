create table universe.user
(
	id bigint auto_increment comment 'id'
		primary key,
	user_name varchar(255) null comment '用户昵称
',
	user_account varchar(256) null comment '用户账号',
	avatar_url varchar(1024) null comment '用户头像
',
	gender tinyint null comment '性别',
	user_password varchar(512) not null comment '用户密码',
	phone varchar(128) null comment '电话',
	email varchar(512) null comment '邮箱',
	user_status int default 0 null comment '状态 0-正常',
	create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
	update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
	is_delete tinyint default 0 not null,
	user_role int default 0 null comment '用户权限  0 - 普通用户 1 - 管理员',
	planet_code varchar(512) null comment '星球编号'
)
comment '用户表';


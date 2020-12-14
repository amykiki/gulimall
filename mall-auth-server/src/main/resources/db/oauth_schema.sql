drop table if exists oauth_client_details;
create table oauth_client_details
(
    client_id               varchar(255) primary key,
    resource_ids            varchar(255),
    client_secret           varchar(255),
    scope                   varchar(255),
    authorized_grant_types  varchar(255),
    web_server_redirect_uri varchar(255),
    authorities             varchar(255),
    access_token_validity   integer,
    refresh_token_validity  integer,
    additional_information  varchar(1024),
    autoapprove             varchar(255)
) ENGINE = InnoDB
  default charset = utf8mb4 comment 'OAuth2.0 客户端';

drop table if exists oauth_client_token;
create table oauth_client_token
(
    token_id          varchar(255),
    token             mediumblob,
    authentication_id varchar(255) primary key,
    user_name         varchar(255),
    client_id         varchar(255)
) ENGINE = InnoDB
  default charset = utf8mb4;

drop table if exists oauth_access_token;
create table oauth_access_token
(
    token_id          varchar(255),
    token             mediumblob,
    authentication_id varchar(255) primary key,
    user_name         varchar(255),
    client_id         varchar(255),
    authentication mediumblob,
    refresh_token varchar(255)
) ENGINE = InnoDB
  default charset = utf8mb4
    comment 'OAuth2.0 访问令牌';

drop table if exists oauth_refres_token;
create table oauth_refres_token
(
    token_id          varchar(255),
    token             mediumblob,
    authentication mediumblob
) ENGINE = InnoDB
  default charset = utf8mb4
    comment 'OAuth2.0 刷新令牌';

drop table if exists oauth_code;
create table oauth_code
(
    code varchar(255),
    authentication mediumblob
) ENGINE = InnoDB
  default charset = utf8mb4
    comment 'OAuth2.0 授权码';

drop table if exists oauth_approvals;
create table oauth_approvals
(
    userId varchar(255),
    clientId varchar(255),
    scope varchar(255),
    status varchar(10),
    expiresAt timestamp default current_timestamp,
    lastModifiedAt timestamp default current_timestamp
) ENGINE = InnoDB
  default charset = utf8mb4;
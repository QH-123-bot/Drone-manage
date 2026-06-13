-- 无人机管理系统 — MySQL 数据库建表脚本（schema-mysql.sql）
-- 作用：在 profile=mysql 且 initialization-mode=always 时由 Spring Boot 自动执行，创建无人机业务表。
-- 功能：定义与 SQLite 版语义一致的 drone 表（InnoDB、utf8mb4），供生产或联调环境存储无人机档案数据。

CREATE TABLE IF NOT EXISTS drone (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  model VARCHAR(128),
  serial_number VARCHAR(64),
  max_flight_time INT,
  max_speed DOUBLE,
  status VARCHAR(32),
  remark VARCHAR(512),
  created_at BIGINT,
  updated_at BIGINT,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

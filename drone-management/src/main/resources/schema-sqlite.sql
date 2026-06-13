-- 无人机管理系统 — SQLite 数据库建表脚本（schema-sqlite.sql）
-- 作用：在 profile=sqlite 且 initialization-mode=always 时由 Spring Boot 自动执行，创建无人机业务表。
-- 功能：定义 drone 表结构（主键自增、名称、型号、序列号、续航、速度、状态、备注、时间戳等列），供 MyBatis 持久化使用。

CREATE TABLE IF NOT EXISTS drone (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  model TEXT,
  serial_number TEXT,
  max_flight_time INTEGER,
  max_speed REAL,
  status TEXT,
  remark TEXT,
  created_at INTEGER,
  updated_at INTEGER
);

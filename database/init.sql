-- 租房管理系统数据库设计
-- PostgreSQL 13+
-- 用户表
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(100) UNIQUE,
  phone VARCHAR(20),
  real_name VARCHAR(50),
  id_card VARCHAR(18),
  role VARCHAR(20) NOT NULL DEFAULT 'TENANT',
  -- ADMIN, LANDLORD, TENANT
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  -- ACTIVE, INACTIVE, SUSPENDED
  avatar_url VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 房屋表
CREATE TABLE houses (
  id BIGSERIAL PRIMARY KEY,
  house_name VARCHAR(100) NOT NULL,
  address TEXT NOT NULL,
  total_rooms INTEGER NOT NULL DEFAULT 0,
  total_area DECIMAL(10, 2),
  house_type VARCHAR(20),
  -- APARTMENT, VILLA, OFFICE
  description TEXT,
  facilities TEXT ,
  -- 设施列表
  images TEXT ,
  -- 图片URL列表
  landlord_id BIGINT REFERENCES users(id),
  status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
  -- AVAILABLE, OCCUPIED, MAINTENANCE
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 房间表
CREATE TABLE rooms (
  id BIGSERIAL PRIMARY KEY,
  house_id BIGINT REFERENCES houses(id) ON DELETE CASCADE,
  room_number VARCHAR(20) NOT NULL,
  room_type VARCHAR(20),
  -- SINGLE, DOUBLE, SUITE
  area DECIMAL(8, 2),
  monthly_rent DECIMAL(10, 2) NOT NULL,
  deposit DECIMAL(10, 2) DEFAULT 0,
  facilities TEXT [],
  -- 房间设施
  images TEXT [],
  -- 房间图片
  status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
  -- AVAILABLE, OCCUPIED, MAINTENANCE
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(house_id, room_number)
);

-- 租户表
CREATE TABLE tenants (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
  emergency_contact VARCHAR(50),
  emergency_phone VARCHAR(20),
  occupation VARCHAR(50),
  company VARCHAR(100),
  move_in_date DATE,
  move_out_date DATE,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  -- ACTIVE, INACTIVE, MOVED_OUT
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 合同表
CREATE TABLE contracts (
  id BIGSERIAL PRIMARY KEY,
  contract_number VARCHAR(50) UNIQUE NOT NULL,
  house_id BIGINT REFERENCES houses(id),
  room_id BIGINT REFERENCES rooms(id),
  tenant_id BIGINT REFERENCES tenants(id),
  landlord_id BIGINT REFERENCES users(id),
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  monthly_rent DECIMAL(10, 2) NOT NULL,
  deposit DECIMAL(10, 2) DEFAULT 0,
  payment_day INTEGER NOT NULL DEFAULT 1,
  -- 每月缴费日
  contract_terms TEXT,
  -- 合同条款
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  -- ACTIVE, EXPIRED, TERMINATED
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 水电费记录表
CREATE TABLE utility_records (
  id BIGSERIAL PRIMARY KEY,
  contract_id BIGINT REFERENCES contracts(id),
  room_id BIGINT REFERENCES rooms(id),
  record_date DATE NOT NULL,
  water_reading DECIMAL(10, 2),
  -- 水表读数
  electricity_reading DECIMAL(10, 2),
  -- 电表读数
  gas_reading DECIMAL(10, 2),
  -- 燃气表读数
  water_usage DECIMAL(10, 2),
  -- 用水量
  electricity_usage DECIMAL(10, 2),
  -- 用电量
  gas_usage DECIMAL(10, 2),
  -- 用气量
  water_price DECIMAL(8, 4) DEFAULT 3.5,
  -- 水费单价
  electricity_price DECIMAL(8, 4) DEFAULT 0.6,
  -- 电费单价
  gas_price DECIMAL(8, 4) DEFAULT 2.5,
  -- 燃气费单价
  water_fee DECIMAL(10, 2) DEFAULT 0,
  -- 水费
  electricity_fee DECIMAL(10, 2) DEFAULT 0,
  -- 电费
  gas_fee DECIMAL(10, 2) DEFAULT 0,
  -- 燃气费
  total_fee DECIMAL(10, 2) DEFAULT 0,
  -- 总费用
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  -- PENDING, PAID, OVERDUE
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 租金记录表
CREATE TABLE rent_records (
  id BIGSERIAL PRIMARY KEY,
  contract_id BIGINT REFERENCES contracts(id),
  tenant_id BIGINT REFERENCES tenants(id),
  room_id BIGINT REFERENCES rooms(id),
  rent_month VARCHAR(7) NOT NULL,
  -- 格式: 2024-01
  rent_amount DECIMAL(10, 2) NOT NULL,
  utility_fee DECIMAL(10, 2) DEFAULT 0,
  other_fee DECIMAL(10, 2) DEFAULT 0,
  total_amount DECIMAL(10, 2) NOT NULL,
  payment_date DATE,
  payment_method VARCHAR(20),
  -- CASH, BANK_TRANSFER, ALIPAY, WECHAT
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  -- PENDING, PAID, OVERDUE
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 费用配置表
CREATE TABLE fee_configs (
  id BIGSERIAL PRIMARY KEY,
  config_name VARCHAR(50) NOT NULL,
  config_type VARCHAR(20) NOT NULL,
  -- WATER, ELECTRICITY, GAS, OTHER
  price DECIMAL(8, 4) NOT NULL,
  unit VARCHAR(10) NOT NULL,
  -- 单位: 元/度, 元/立方米等
  effective_date DATE NOT NULL,
  expire_date DATE,
  description TEXT,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 系统日志表
CREATE TABLE system_logs (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  action VARCHAR(50) NOT NULL,
  module VARCHAR(30) NOT NULL,
  description TEXT,
  ip_address VARCHAR(45),
  user_agent TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_users_username ON users(username);

CREATE INDEX idx_users_email ON users(email);

CREATE INDEX idx_users_role ON users(role);

CREATE INDEX idx_houses_landlord ON houses(landlord_id);

CREATE INDEX idx_houses_status ON houses(status);

CREATE INDEX idx_rooms_house ON rooms(house_id);

CREATE INDEX idx_rooms_status ON rooms(status);

CREATE INDEX idx_tenants_user ON tenants(user_id);

CREATE INDEX idx_contracts_house ON contracts(house_id);

CREATE INDEX idx_contracts_room ON contracts(room_id);

CREATE INDEX idx_contracts_tenant ON contracts(tenant_id);

CREATE INDEX idx_contracts_status ON contracts(status);

CREATE INDEX idx_utility_records_contract ON utility_records(contract_id);

CREATE INDEX idx_utility_records_date ON utility_records(record_date);

CREATE INDEX idx_rent_records_contract ON rent_records(contract_id);

CREATE INDEX idx_rent_records_month ON rent_records(rent_month);

CREATE INDEX idx_rent_records_status ON rent_records(status);

CREATE INDEX idx_fee_configs_type ON fee_configs(config_type);

CREATE INDEX idx_fee_configs_active ON fee_configs(is_active);

CREATE INDEX idx_system_logs_user ON system_logs(user_id);

CREATE INDEX idx_system_logs_created ON system_logs(created_at);

-- 插入初始数据
INSERT INTO
  users (
    username,
    password,
    email,
    real_name,
    role,
    status
  )
VALUES
  (
    'admin',
    '$2a$10$aXyrACJ26rTbgqR2dMLg.uO5MLkH3LhLXBdbTT4WCId9Gz7gWNNhy',
    -- 这是'admin'经过BCrypt加密后的值
    'admin@zufang.com',
    '系统管理员',
    'ADMIN',
    'ACTIVE'
  );

INSERT INTO
  fee_configs (
    config_name,
    config_type,
    price,
    unit,
    effective_date
  )
VALUES
  ('水费标准', 'WATER', 3.5, '元/立方米', '2024-01-01'),
  ('电费标准', 'ELECTRICITY', 0.6, '元/度', '2024-01-01'),
  ('燃气费标准', 'GAS', 2.5, '元/立方米', '2024-01-01');
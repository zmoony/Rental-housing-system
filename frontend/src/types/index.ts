// 用户相关类型
export interface User {
  id: number;
  username: string;
  email?: string;
  phone?: string;
  realName?: string;
  idCard?: string;
  role: "ADMIN" | "LANDLORD" | "TENANT";
  status: "ACTIVE" | "INACTIVE" | "SUSPENDED";
  avatarUrl?: string;
  createdAt: string;
  updatedAt: string;
}

// 认证相关类型
export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

// 房屋相关类型
export interface House {
  id: number;
  houseName: string;
  address: string;
  totalRooms: number;
  totalArea?: number;
  houseType?: string;
  description?: string;
  facilities?: string[];
  images?: string[];
  landlordId: number;
  status: "AVAILABLE" | "OCCUPIED" | "MAINTENANCE";
  createdAt: string;
  updatedAt: string;
}

// 房间相关类型
export interface Room {
  id: number;
  houseId: number;
  roomNumber: string;
  roomType?: string;
  area?: number;
  monthlyRent: number;
  deposit?: number;
  facilities?: string[];
  images?: string[];
  status: "AVAILABLE" | "OCCUPIED" | "MAINTENANCE";
  createdAt: string;
  updatedAt: string;
}

// 租户相关类型
export interface Tenant {
  id: number;
  userId: number;
  emergencyContact?: string;
  emergencyPhone?: string;
  occupation?: string;
  company?: string;
  moveInDate?: string;
  moveOutDate?: string;
  status: "ACTIVE" | "INACTIVE" | "MOVED_OUT";
  createdAt: string;
  updatedAt: string;
}

// 合同相关类型
export interface Contract {
  id: number;
  contractNumber: string;
  houseId: number;
  roomId: number;
  tenantId: number;
  landlordId: number;
  startDate: string;
  endDate: string;
  monthlyRent: number;
  deposit?: number;
  paymentDay: number;
  contractTerms?: string;
  status: "ACTIVE" | "EXPIRED" | "TERMINATED";
  createdAt: string;
  updatedAt: string;
}

// 水电费记录类型
export interface UtilityRecord {
  id: number;
  contractId: number;
  roomId: number;
  recordDate: string;
  waterReading?: number;
  electricityReading?: number;
  gasReading?: number;
  waterUsage?: number;
  electricityUsage?: number;
  gasUsage?: number;
  waterPrice?: number;
  electricityPrice?: number;
  gasPrice?: number;
  waterFee?: number;
  electricityFee?: number;
  gasFee?: number;
  totalFee?: number;
  status: "PENDING" | "PAID" | "OVERDUE";
  createdAt: string;
  updatedAt: string;
}

// 租金记录类型
export interface RentRecord {
  id: number;
  contractId: number;
  tenantId: number;
  roomId: number;
  rentMonth: string;
  rentAmount: number;
  utilityFee?: number;
  otherFee?: number;
  totalAmount: number;
  paymentDate?: string;
  paymentMethod?: string;
  status: "PENDING" | "PAID" | "OVERDUE";
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

// API 响应类型
export interface ApiResponse<T> {
  code: string;
  message: string;
  data: T;
}

// 分页响应类型
export interface PageResponse<T> {
  list: T[];
  total: number;
  page: number;
  size: number;
  pages: number;
}

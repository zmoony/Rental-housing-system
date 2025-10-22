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

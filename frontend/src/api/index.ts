import api from "./request";
import type { LoginRequest, LoginResponse, User } from "@/types";

// 用户认证相关 API
export const login = (username: string, password: string): Promise<LoginResponse> => {
  return api.post("/auth/login", { username, password });
};

export const logout = (): Promise<void> => {
  return api.post("/auth/logout");
};

export const getUserInfo = (): Promise<User> => {
  return api.get("/auth/user-info");
};

export const changePassword = (param:any) => {
  return api.post("/auth/change-password", param);
};

// 合同管理 API
export const createContract = (contract: any) => {
  return api.post("/contracts", contract);
};

export const getContract = (id: number) => {
  return api.get(`/contracts/${id}`);
};

export const renewContract = (id: number, newEndDate: string) => {
  return api.put(`/contracts/${id}/renew`, null, {
    params: { newEndDate }
  });
};

export const terminateContract = (id: number, reason: string) => {
  return api.put(`/contracts/${id}/terminate`, null, {
    params: { reason }
  });
};

export const getTenantContracts = (tenantId: number) => {
  return api.get(`/contracts/tenant/${tenantId}`);
};

export const getLandlordContracts = (landlordId: number) => {
  return api.get(`/contracts/landlord/${landlordId}`);
};

// 水电费管理 API
export const recordUtilityReading = (data: any) => {
  return api.post("/utilities/record", data);
};

export const getRoomUtilityRecords = (roomId: number) => {
  return api.get(`/utilities/room/${roomId}`);
};

export const getContractUtilityRecords = (contractId: number) => {
  return api.get(`/utilities/contract/${contractId}`);
};

export const generateMonthlyBills = () => {
  return api.post("/utilities/generate-monthly-bills");
};

// 房屋管理 API
export const getHouses = (params?: any) => {
  return api.get("/houses", { params });
};

export const createHouse = (house: any) => {
  return api.post("/houses", house);
};

export const updateHouse = (id: number, house: any) => {
  return api.put(`/houses/${id}`, house);
};

export const deleteHouse = (id: number) => {
  return api.delete(`/houses/${id}`);
};

// 房间管理 API
export const getRooms = (params?: any) => {
  return api.get("/rooms", { params });
};

export const createRoom = (room: any) => {
  return api.post("/rooms", room);
};

export const updateRoom = (id: number, room: any) => {
  return api.put(`/rooms/${id}`, room);
};

export const deleteRoom = (id: number) => {
  return api.delete(`/rooms/${id}`);
};

// 租户管理 API
export const getTenants = (params?: any) => {
  return api.get("/tenants", { params });
};

export const createTenant = (tenant: any) => {
  return api.post("/tenants", tenant);
};

export const updateTenant = (id: number, tenant: any) => {
  return api.put(`/tenants/${id}`, tenant);
};

export const deleteTenant = (id: number) => {
  return api.delete(`/tenants/${id}`);
};

import request from '@/utils/request';

export interface Tenant {
  id: number;
  userId: number;
  name: string;
  phone: string;
  idCard: string;
  gender: string;
  age: number;
  occupation: string;
  emergencyContact: string;
  emergencyPhone: string;
  status: string;
  createTime: string;
  updateTime: string;
}

export interface TenantQuery {
  current: number;
  size: number;
  keyword?: string;
  userId?: number;
  status?: string;
}

/**
 * 获取租户列表（分页）
 */
export function getTenantList(params: TenantQuery) {
  return request({
    url: '/tenant/list',
    method: 'get',
    params
  });
}

/**
 * 获取租户详情
 */
export function getTenantDetail(id: number) {
  return request({
    url: `/tenant/${id}`,
    method: 'get'
  });
}

/**
 * 添加租户
 */
export function addTenant(data: Tenant) {
  return request({
    url: '/tenant',
    method: 'post',
    data
  });
}

/**
 * 更新租户
 */
export function updateTenant(data: Tenant) {
  return request({
    url: '/tenant',
    method: 'put',
    data
  });
}

/**
 * 删除租户
 */
export function deleteTenant(id: number) {
  return request({
    url: `/tenant/${id}`,
    method: 'delete'
  });
}

/**
 * 更新租户状态
 */
export function updateTenantStatus(id: number, status: string) {
  return request({
    url: `/tenant/status/${id}/${status}`,
    method: 'put'
  });
}
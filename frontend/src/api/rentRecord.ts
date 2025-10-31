import request from '@/utils/request';

export interface RentRecord {
  id: number;
  contractId: number;
  tenantId: number;
  tenantName: string;
  roomId: number;
  roomNumber: string;
  month: string;
  rentAmount: number;
  waterFee: number;
  electricityFee: number;
  gasFee: number;
  propertyFee: number;
  otherFee: number;
  totalAmount: number;
  status: string;
  paymentTime: string;
  createTime: string;
  updateTime: string;
}

export interface RentRecordQuery {
  current: number;
  size: number;
  contractId?: number;
  tenantId?: number;
  roomId?: number;
  month?: string;
  status?: string;
}

/**
 * 获取租金记录列表（分页）
 */
export function getRentRecordList(params: RentRecordQuery) {
  return request({
    url: '/rentRecord/list',
    method: 'get',
    params
  });
}

/**
 * 获取租金记录详情
 */
export function getRentRecordDetail(id: number) {
  return request({
    url: `/rentRecord/${id}`,
    method: 'get'
  });
}

/**
 * 添加租金记录
 */
export function addRentRecord(data: RentRecord) {
  return request({
    url: '/rentRecord',
    method: 'post',
    data
  });
}

/**
 * 更新租金记录
 */
export function updateRentRecord(data: RentRecord) {
  return request({
    url: '/rentRecord',
    method: 'put',
    data
  });
}

/**
 * 删除租金记录
 */
export function deleteRentRecord(id: number) {
  return request({
    url: `/rentRecord/${id}`,
    method: 'delete'
  });
}

/**
 * 更新租金记录状态
 */
export function updateRentRecordStatus(id: number, status: string) {
  return request({
    url: `/rentRecord/status/${id}/${status}`,
    method: 'put'
  });
}
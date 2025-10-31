import request from '@/utils/request';

export interface SystemLog {
  id: number;
  operationType: string;
  operationContent: string;
  operator: string;
  operationTime: string;
  operationResult: string;
  operationIp: string;
}

export interface SystemLogQuery {
  current: number;
  size: number;
  operationType?: string;
  operator?: string;
  startTime?: string;
  endTime?: string;
}

/**
 * 获取系统日志列表（分页）
 */
export function getSystemLogList(params: SystemLogQuery) {
  return request({
    url: '/systemLog/list',
    method: 'get',
    params
  });
}

/**
 * 获取系统日志详情
 */
export function getSystemLogDetail(id: number) {
  return request({
    url: `/systemLog/${id}`,
    method: 'get'
  });
}

/**
 * 删除系统日志
 */
export function deleteSystemLog(id: number) {
  return request({
    url: `/systemLog/${id}`,
    method: 'delete'
  });
}

/**
 * 批量删除系统日志
 */
export function batchDeleteSystemLog(ids: number[]) {
  return request({
    url: '/systemLog/batch',
    method: 'delete',
    data: ids
  });
}

/**
 * 清空系统日志
 */
export function clearSystemLog() {
  return request({
    url: '/systemLog/clear',
    method: 'delete'
  });
}
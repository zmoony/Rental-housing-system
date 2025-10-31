import request from '@/utils/request';

export interface FeeConfig {
  id: number;
  feeType: string;
  feeName: string;
  feeStandard: number;
  houseId: number;
  houseName: string;
  status: string;
  createTime: string;
  updateTime: string;
}

export interface FeeConfigQuery {
  current: number;
  size: number;
  feeType?: string;
  houseId?: number;
  status?: string;
}

/**
 * 获取费用配置列表（分页）
 */
export function getFeeConfigList(params: FeeConfigQuery) {
  return request({
    url: '/feeConfig/list',
    method: 'get',
    params
  });
}

/**
 * 获取费用配置详情
 */
export function getFeeConfigDetail(id: number) {
  return request({
    url: `/feeConfig/${id}`,
    method: 'get'
  });
}

/**
 * 添加费用配置
 */
export function addFeeConfig(data: FeeConfig) {
  return request({
    url: '/feeConfig',
    method: 'post',
    data
  });
}

/**
 * 更新费用配置
 */
export function updateFeeConfig(data: FeeConfig) {
  return request({
    url: '/feeConfig',
    method: 'put',
    data
  });
}

/**
 * 删除费用配置
 */
export function deleteFeeConfig(id: number) {
  return request({
    url: `/feeConfig/${id}`,
    method: 'delete'
  });
}

/**
 * 更新费用配置状态
 */
export function updateFeeConfigStatus(id: number, status: string) {
  return request({
    url: `/feeConfig/status/${id}/${status}`,
    method: 'put'
  });
}
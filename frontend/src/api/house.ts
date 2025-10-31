import request from './request';

export interface House {
  id?: number;
  houseName: string;
  address: string;
  totalRooms: number;
  totalArea: number;
  houseType: string;
  description: string;
  facilities: string;
  images: string;
  landlordId: number;
  status: string;
}

export interface HouseQuery {
  current: number;
  size: number;
  keyword?: string;
}

// 分页获取房屋列表
export function getHouseList(params: HouseQuery) {
  return request({
    url: '/houses/page',
    method: 'get',
    params
  });
}

// 获取房屋详情
export function getHouseDetail(id: number) {
  return request({
    url: `/houses/${id}`,
    method: 'get'
  });
}

// 添加房屋
export function addHouse(data: House) {
  return request({
    url: '/houses',
    method: 'post',
    data
  });
}

// 更新房屋
export function updateHouse(data: House) {
  return request({
    url: '/houses',
    method: 'put',
    data
  });
}

// 删除房屋
export function deleteHouse(id: number) {
  return request({
    url: `/houses/${id}`,
    method: 'delete'
  });
}

// 更新房屋状态
export function updateHouseStatus(id: number, status: string) {
  return request({
    url: `/houses/${id}/status/${status}`,
    method: 'put'
  });
}
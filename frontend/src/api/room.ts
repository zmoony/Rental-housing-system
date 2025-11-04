import request from './request';

export interface Room {
  id?: number;
  houseId: number;
  houseName?: string; // 前端展示用
  roomNumber: string;
  roomType: string;
  area: number;
  monthlyRent: number;
  deposit: number;
  status: string;
  facilities: string;
  description: string;
  images: string;
}

export interface RoomQuery {
  current: number;
  size: number;
  keyword?: string;
  houseId?: number;
  status?: string;
}

// 分页获取房间列表
export function getRoomList(params: RoomQuery) {
  return request({
    url: '/rooms/page',
    method: 'get',
    params
  });
}

// 获取房间详情
export function getRoomDetail(id: number) {
  return request({
    url: `/rooms/${id}`,
    method: 'get'
  });
}

// 根据房屋ID获取房间列表
export function getRoomsByHouseId(houseId: number) {
  return request({
    url: `/rooms/house/${houseId}`,
    method: 'get'
  });
}

// 添加房间
export function addRoom(data: Room) {
  return request({
    url: '/rooms',
    method: 'post',
    data
  });
}

// 更新房间
export function updateRoom(data: Room) {
  return request({
    url: '/rooms',
    method: 'put',
    data
  });
}

// 删除房间
export function deleteRoom(id: number) {
  return request({
    url: `/rooms/${id}`,
    method: 'delete'
  });
}

// 更新房间状态
export function updateRoomStatus(id: number, status: string) {
  return request({
    url: `/rooms/${id}/status/${status}`,
    method: 'put'
  });
}
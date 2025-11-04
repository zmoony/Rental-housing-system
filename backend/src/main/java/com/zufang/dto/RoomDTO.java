package com.zufang.dto;

import com.zufang.entity.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoomDTO extends Room {
    private String houseName;
}

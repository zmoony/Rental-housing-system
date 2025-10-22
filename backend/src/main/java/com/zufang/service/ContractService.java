package com.zufang.service;

import com.zufang.entity.Contract;
import com.zufang.entity.Room;
import com.zufang.entity.Tenant;
import com.zufang.mapper.ContractMapper;
import com.zufang.mapper.RoomMapper;
import com.zufang.mapper.TenantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 合同管理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractMapper contractMapper;
    private final RoomMapper roomMapper;
    private final TenantMapper tenantMapper;

    /**
     * 创建合同
     */
    @Transactional
    public Contract createContract(Contract contract) {
        // 验证房间是否可用
        Room room = roomMapper.selectById(contract.getRoomId());
        if (room == null || !"AVAILABLE".equals(room.getStatus())) {
            throw new RuntimeException("房间不可用");
        }

        // 验证租户是否存在
        Tenant tenant = tenantMapper.selectById(contract.getTenantId());
        if (tenant == null || !"ACTIVE".equals(tenant.getStatus())) {
            throw new RuntimeException("租户状态异常");
        }

        // 生成合同编号
        String contractNumber = generateContractNumber();
        contract.setContractNumber(contractNumber);

        // 设置合同状态
        contract.setStatus("ACTIVE");

        // 保存合同
        contractMapper.insert(contract);

        // 更新房间状态
        room.setStatus("OCCUPIED");
        roomMapper.updateById(room);

        // 更新租户状态
        tenant.setStatus("ACTIVE");
        tenant.setMoveInDate(contract.getStartDate());
        tenantMapper.updateById(tenant);

        log.info("合同创建成功 - 合同编号: {}, 房间ID: {}, 租户ID: {}", 
                contractNumber, contract.getRoomId(), contract.getTenantId());

        return contract;
    }

    /**
     * 续签合同
     */
    @Transactional
    public Contract renewContract(Long contractId, LocalDate newEndDate) {
        Contract contract = contractMapper.selectById(contractId);
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }

        if (!"ACTIVE".equals(contract.getStatus())) {
            throw new RuntimeException("合同状态异常，无法续签");
        }

        // 更新合同结束日期
        contract.setEndDate(newEndDate);
        contractMapper.updateById(contract);

        log.info("合同续签成功 - 合同ID: {}, 新结束日期: {}", contractId, newEndDate);

        return contract;
    }

    /**
     * 终止合同
     */
    @Transactional
    public void terminateContract(Long contractId, String reason) {
        Contract contract = contractMapper.selectById(contractId);
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }

        // 更新合同状态
        contract.setStatus("TERMINATED");
        contractMapper.updateById(contract);

        // 更新房间状态
        Room room = roomMapper.selectById(contract.getRoomId());
        if (room != null) {
            room.setStatus("AVAILABLE");
            roomMapper.updateById(room);
        }

        // 更新租户状态
        Tenant tenant = tenantMapper.selectById(contract.getTenantId());
        if (tenant != null) {
            tenant.setStatus("MOVED_OUT");
            tenant.setMoveOutDate(LocalDate.now());
            tenantMapper.updateById(tenant);
        }

        log.info("合同终止成功 - 合同ID: {}, 原因: {}", contractId, reason);
    }

    /**
     * 检查合同到期
     */
    @Transactional
    public void checkExpiredContracts() {
        LocalDate today = LocalDate.now();
        List<Contract> expiredContracts = contractMapper.getExpiredContracts(today);

        for (Contract contract : expiredContracts) {
            // 更新合同状态为过期
            contract.setStatus("EXPIRED");
            contractMapper.updateById(contract);

            // 更新房间状态
            Room room = roomMapper.selectById(contract.getRoomId());
            if (room != null) {
                room.setStatus("AVAILABLE");
                roomMapper.updateById(room);
            }

            log.info("合同已过期 - 合同ID: {}, 结束日期: {}", contract.getId(), contract.getEndDate());
        }
    }

    /**
     * 生成合同编号
     */
    private String generateContractNumber() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "HT" + dateStr + uuid;
    }

    /**
     * 获取合同详情
     */
    public Contract getContractDetails(Long contractId) {
        return contractMapper.selectById(contractId);
    }

    /**
     * 获取租户的合同列表
     */
    public List<Contract> getTenantContracts(Long tenantId) {
        return contractMapper.getContractsByTenant(tenantId);
    }

    /**
     * 获取房东的合同列表
     */
    public List<Contract> getLandlordContracts(Long landlordId) {
        return contractMapper.getContractsByLandlord(landlordId);
    }
}

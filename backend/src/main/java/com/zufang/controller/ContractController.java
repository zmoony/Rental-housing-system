package com.zufang.controller;

import com.zufang.annotation.LogOperation;
import com.zufang.common.ApiResponse;
import com.zufang.entity.Contract;
import com.zufang.entity.UtilityRecord;
import com.zufang.service.ContractService;
import com.zufang.service.UtilityCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

/**
 * 合同管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    /**
     * 创建合同
     */
    @PostMapping
    @LogOperation(operationType = "新增", operationDesc = "创建合同")
    public ResponseEntity<ApiResponse<Contract>> createContract(@Valid @RequestBody Contract contract) {
        Contract createdContract = contractService.createContract(contract);
        return ResponseEntity.ok(ApiResponse.success(createdContract));
    }

    /**
     * 获取合同详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Contract>> getContract(@PathVariable Long id) {
        Contract contract = contractService.getContractDetails(id);
        return ResponseEntity.ok(ApiResponse.success(contract));
    }

    /**
     * 续签合同
     */
    @PutMapping("/{id}/renew")
    @LogOperation(operationType = "修改", operationDesc = "续签合同")
    public ResponseEntity<Contract> renewContract(@PathVariable Long id,
                                                @RequestParam LocalDate newEndDate) {
        Contract contract = contractService.renewContract(id, newEndDate);
        return ResponseEntity.ok(contract);
    }

    /**
     * 终止合同
     */
    @PutMapping("/{id}/terminate")
    @LogOperation(operationType = "修改", operationDesc = "终止合同")
    public ResponseEntity<Void> terminateContract(@PathVariable Long id,
                                               @RequestParam String reason) {
        contractService.terminateContract(id, reason);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取租户合同列表
     */
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<ApiResponse<List<Contract>>> getTenantContracts(@PathVariable Long tenantId) {
        List<Contract> contracts = contractService.getTenantContracts(tenantId);
        return ResponseEntity.ok(ApiResponse.success(contracts));
    }

    /**
     * 获取房东合同列表
     */
    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<List<Contract>> getLandlordContracts(@PathVariable Long landlordId) {
        List<Contract> contracts = contractService.getLandlordContracts(landlordId);
        return ResponseEntity.ok(contracts);
    }
}

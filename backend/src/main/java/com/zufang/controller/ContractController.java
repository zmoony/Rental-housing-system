package com.zufang.controller;

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
    public ResponseEntity<Contract> createContract(@Valid @RequestBody Contract contract) {
        Contract createdContract = contractService.createContract(contract);
        return ResponseEntity.ok(createdContract);
    }

    /**
     * 获取合同详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable Long id) {
        Contract contract = contractService.getContractDetails(id);
        return ResponseEntity.ok(contract);
    }

    /**
     * 续签合同
     */
    @PutMapping("/{id}/renew")
    public ResponseEntity<Contract> renewContract(@PathVariable Long id,
                                                @RequestParam LocalDate newEndDate) {
        Contract contract = contractService.renewContract(id, newEndDate);
        return ResponseEntity.ok(contract);
    }

    /**
     * 终止合同
     */
    @PutMapping("/{id}/terminate")
    public ResponseEntity<Void> terminateContract(@PathVariable Long id,
                                               @RequestParam String reason) {
        contractService.terminateContract(id, reason);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取租户合同列表
     */
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Contract>> getTenantContracts(@PathVariable Long tenantId) {
        List<Contract> contracts = contractService.getTenantContracts(tenantId);
        return ResponseEntity.ok(contracts);
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

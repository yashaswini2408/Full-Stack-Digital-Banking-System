package com.maverickbank.service;

import com.maverickbank.entity.Bank;
import com.maverickbank.entity.Branch;
import com.maverickbank.repository.BankRepository;
import com.maverickbank.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {
    
    @Autowired
    private BankRepository bankRepository;
    
    @Autowired
    private BranchRepository branchRepository;

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Optional<Bank> getBankById(Long id) {
        return bankRepository.findById(id);
    }

    public Bank saveBank(Bank bank) {
        return bankRepository.save(bank);
    }

    public List<Branch> getBranchesByBankName(String bankName) {
        return branchRepository.findByBankBankName(bankName);
    }

    public List<Branch> getBranchesByBankId(Long bankId) {
        return branchRepository.findByBankId(bankId);
    }

    public Optional<Branch> getBranchByIfsc(String ifscCode) {
        return branchRepository.findByIfscCode(ifscCode);
    }

    public Branch saveBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }
}

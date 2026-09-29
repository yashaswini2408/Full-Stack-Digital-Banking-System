package com.maverickbank.service;

import com.maverickbank.entity.Beneficiary;
import com.maverickbank.entity.User;
import com.maverickbank.repository.BeneficiaryRepository;
import com.maverickbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BeneficiaryService {
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Beneficiary> getBeneficiariesByUserId(Long userId) {
        return beneficiaryRepository.findByUserId(userId);
    }

    public Beneficiary addBeneficiary(Long userId, String name, String accountNumber, String bankName, String branchName, String ifscCode) {
        User user = userRepository.findById(userId).orElseThrow();
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName(name);
        beneficiary.setAccountNumber(accountNumber);
        beneficiary.setBankName(bankName);
        beneficiary.setBranchName(branchName);
        beneficiary.setIfscCode(ifscCode);
        beneficiary.setUser(user);
        return beneficiaryRepository.save(beneficiary);
    }

    public void deleteBeneficiary(Long beneficiaryId) {
        beneficiaryRepository.deleteById(beneficiaryId);
    }
}

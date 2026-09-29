package com.maverickbank.service;

import com.maverickbank.entity.LoanType;
import com.maverickbank.repository.LoanTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LoanTypeService {
    
    @Autowired
    private LoanTypeRepository loanTypeRepository;

    public List<LoanType> getAllActiveLoanTypes() {
        return loanTypeRepository.findByIsActiveTrue();
    }

    public List<LoanType> getAllLoanTypes() {
        return loanTypeRepository.findAll();
    }

    public Optional<LoanType> getLoanTypeById(Long id) {
        return loanTypeRepository.findById(id);
    }

    public LoanType saveLoanType(LoanType loanType) {
        return loanTypeRepository.save(loanType);
    }

    public void deleteLoanType(Long id) {
        LoanType loanType = loanTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Loan type not found"));
        loanType.setIsActive(false);
        loanTypeRepository.save(loanType);
    }
}

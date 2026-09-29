package com.maverickbank.service;

import com.maverickbank.entity.Loan;
import com.maverickbank.entity.LoanType;
import com.maverickbank.entity.User;
import com.maverickbank.repository.LoanRepository;
import com.maverickbank.repository.LoanTypeRepository;
import com.maverickbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoanTypeRepository loanTypeRepository;

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getPendingLoans() {
        return loanRepository.findByStatus("APPLIED");
    }

    public Loan getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));
    }

    public List<Loan> getLoansByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return loanRepository.findByUserId(userId);
    }

    public Loan applyLoan(Long userId, Long loanTypeId, BigDecimal amount, int tenureMonths, String purpose) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        LoanType loanType = loanTypeRepository.findById(loanTypeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan type not found"));

        // Validate loan amount against loan type limits
        if (amount.compareTo(loanType.getMaxAmount()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Loan amount exceeds maximum allowed for this loan type");
        }

        // Validate tenure
        if (tenureMonths > loanType.getMaxTenureMonths()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Loan tenure exceeds maximum allowed for this loan type");
        }

        Loan loan = new Loan();
        loan.setAmount(amount);
        loan.setInterestRate(loanType.getInterestRate());
        loan.setTenureMonths(tenureMonths);
        loan.setStatus("APPLIED");
        loan.setApplicationDate(LocalDate.now());
        loan.setUser(user);
        loan.setLoanType(loanType);
        loan.setPurpose(purpose);
        
        return loanRepository.save(loan);
    }

    public Loan approveLoan(Long loanId, Long employeeId, String comments) {
        Loan loan = getLoanById(loanId);
        User employee = userRepository.findById(employeeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        if (!"APPLIED".equals(loan.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Only loans with APPLIED status can be approved");
        }

        loan.setStatus("APPROVED");
        loan.setApprovedBy(employee);
        loan.setEmployeeComments(comments);
        
        return loanRepository.save(loan);
    }

    public Loan rejectLoan(Long loanId, Long employeeId, String comments) {
        Loan loan = getLoanById(loanId);
        User employee = userRepository.findById(employeeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        if (!"APPLIED".equals(loan.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Only loans with APPLIED status can be rejected");
        }

        loan.setStatus("REJECTED");
        loan.setApprovedBy(employee);
        loan.setEmployeeComments(comments);
        
        return loanRepository.save(loan);
    }

    public Loan disburseLoan(Long loanId, Long employeeId) {
        Loan loan = getLoanById(loanId);
        User employee = userRepository.findById(employeeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        if (!"APPROVED".equals(loan.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Only approved loans can be disbursed");
        }

        loan.setStatus("DISBURSED");
        // In a real system, you would also credit the loan amount to user's account
        
        return loanRepository.save(loan);
    }

    public BigDecimal calculateEMI(BigDecimal principal, double annualRate, int tenureMonths) {
        double monthlyRate = annualRate / 12 / 100;
        if (monthlyRate == 0) {
            return principal.divide(BigDecimal.valueOf(tenureMonths));
        }
        
        double emi = principal.doubleValue() * monthlyRate * 
                    Math.pow(1 + monthlyRate, tenureMonths) / 
                    (Math.pow(1 + monthlyRate, tenureMonths) - 1);
        
        return BigDecimal.valueOf(emi);
    }

    public boolean checkLoanEligibility(Long userId, BigDecimal requestedAmount) {
        // Simple eligibility check based on existing loans
        List<Loan> existingLoans = getLoansByUserId(userId);
        BigDecimal totalOutstandingLoans = existingLoans.stream()
            .filter(loan -> "DISBURSED".equals(loan.getStatus()))
            .map(Loan::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Simple rule: total outstanding loans should not exceed 5 times the requested amount
        return totalOutstandingLoans.compareTo(requestedAmount.multiply(BigDecimal.valueOf(5))) <= 0;
    }
}

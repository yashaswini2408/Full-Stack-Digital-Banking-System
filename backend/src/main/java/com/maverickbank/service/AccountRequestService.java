package com.maverickbank.service;

import com.maverickbank.entity.Account;
import com.maverickbank.entity.AccountRequest;
import com.maverickbank.entity.User;
import com.maverickbank.repository.AccountRequestRepository;
import com.maverickbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountRequestService {
    
    @Autowired
    private AccountRequestRepository accountRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AccountService accountService;

    public AccountRequest submitAccountRequest(Long userId, String accountType, String ifscCode, String branchName, String branchAddress) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        
        AccountRequest request = new AccountRequest();
        request.setUser(user);
        request.setAccountType(accountType);
        request.setIfscCode(ifscCode);
        request.setBranchName(branchName);
        request.setBranchAddress(branchAddress);
        request.setStatus("PENDING");
        request.setRequestDate(LocalDateTime.now());
        
        return accountRequestRepository.save(request);
    }

    public List<AccountRequest> getPendingRequests() {
        return accountRequestRepository.findByStatusOrderByRequestDateDesc("PENDING");
    }

    public List<AccountRequest> getRequestsByUserId(Long userId) {
        return accountRequestRepository.findByUserId(userId);
    }

    public AccountRequest approveRequest(Long requestId, Long employeeId, String comments) {
        AccountRequest request = accountRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        User employee = userRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Create the actual account
        Account account = accountService.openAccount(
            request.getUser().getId(),
            request.getAccountType(),
            request.getIfscCode(),
            request.getBranchName(),
            request.getBranchAddress(),
            BigDecimal.ZERO
        );

        // Update request status
        request.setStatus("APPROVED");
        request.setApprovedBy(employee);
        request.setApprovedDate(LocalDateTime.now());
        request.setEmployeeComments(comments);

        return accountRequestRepository.save(request);
    }

    public AccountRequest rejectRequest(Long requestId, Long employeeId, String comments) {
        AccountRequest request = accountRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        User employee = userRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        request.setStatus("REJECTED");
        request.setApprovedBy(employee);
        request.setApprovedDate(LocalDateTime.now());
        request.setEmployeeComments(comments);

        return accountRequestRepository.save(request);
    }

    public Optional<AccountRequest> getRequestById(Long id) {
        return accountRequestRepository.findById(id);
    }

    public List<AccountRequest> getAllRequests() {
        return accountRequestRepository.findAll();
    }
}

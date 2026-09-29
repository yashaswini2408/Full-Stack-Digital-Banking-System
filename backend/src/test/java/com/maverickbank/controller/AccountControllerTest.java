package com.maverickbank.controller;

import com.maverickbank.entity.Account;
import com.maverickbank.entity.User;
import com.maverickbank.entity.AccountRequest;
import com.maverickbank.service.AccountService;
import com.maverickbank.service.AccountRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRequestService accountRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Account testAccount;
    private AccountRequest testAccountRequest;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        // Setup test account
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACCT1234567890");
        testAccount.setAccountType("SAVINGS");
        testAccount.setBalance(BigDecimal.valueOf(10000.00));
        testAccount.setUser(testUser);
        testAccount.setIfscCode("MVB0001001");
        testAccount.setBranchName("Main Branch");
        testAccount.setBranchAddress("123 Main Street");

        // Setup test account request
        testAccountRequest = new AccountRequest();
        testAccountRequest.setId(1L);
        testAccountRequest.setUser(testUser);
        testAccountRequest.setAccountType("SAVINGS");
        testAccountRequest.setStatus("PENDING");
        testAccountRequest.setRequestDate(LocalDateTime.now());
        testAccountRequest.setIfscCode("MVB0001001");
        testAccountRequest.setBranchName("Main Branch");
        testAccountRequest.setBranchAddress("123 Main Street");
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAccountsByUser_Success() throws Exception {
        // Given
        Long userId = 1L;
        List<Account> accounts = Arrays.asList(testAccount);
        when(accountService.getAccountsByUserId(userId)).thenReturn(accounts);

        // When & Then
        mockMvc.perform(get("/api/accounts/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].accountNumber").value("ACCT1234567890"))
                .andExpect(jsonPath("$[0].accountType").value("SAVINGS"))
                .andExpect(jsonPath("$[0].balance").value(10000.00));

        verify(accountService).getAccountsByUserId(userId);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAccountByNumber_Success() throws Exception {
        // Given
        String accountNumber = "ACCT1234567890";
        when(accountService.getAccountByNumber(accountNumber)).thenReturn(Optional.of(testAccount));

        // When & Then
        mockMvc.perform(get("/api/accounts/{accountNumber}", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACCT1234567890"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.balance").value(10000.00));

        verify(accountService).getAccountByNumber(accountNumber);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAccountByNumber_NotFound() throws Exception {
        // Given
        String accountNumber = "INVALID_ACCOUNT";
        when(accountService.getAccountByNumber(accountNumber)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/accounts/{accountNumber}", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(accountService).getAccountByNumber(accountNumber);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testSubmitAccountRequest_Success() throws Exception {
        // Given
        AccountController.AccountOpenRequestDto requestDto = new AccountController.AccountOpenRequestDto();
        requestDto.setUserId(1L);
        requestDto.setAccountType("SAVINGS");
        requestDto.setIfscCode("MVB0001001");
        requestDto.setBranchName("Main Branch");
        requestDto.setBranchAddress("123 Main Street");

        when(accountRequestService.submitAccountRequest(anyLong(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(testAccountRequest);

        // When & Then
        mockMvc.perform(post("/api/accounts/request")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(accountRequestService).submitAccountRequest(1L, "SAVINGS", "MVB0001001", "Main Branch", "123 Main Street");
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAccountRequestsByUser_Success() throws Exception {
        // Given
        Long userId = 1L;
        List<AccountRequest> requests = Arrays.asList(testAccountRequest);
        when(accountRequestService.getRequestsByUserId(userId)).thenReturn(requests);

        // When & Then
        mockMvc.perform(get("/api/accounts/requests/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].accountType").value("SAVINGS"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(accountRequestService).getRequestsByUserId(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testOpenAccount_Success() throws Exception {
        // Given
        AccountController.AccountOpenRequest openRequest = new AccountController.AccountOpenRequest();
        openRequest.setUserId(1L);
        openRequest.setAccountType("SAVINGS");
        openRequest.setIfscCode("MVB0001001");
        openRequest.setBranchName("Main Branch");
        openRequest.setBranchAddress("123 Main Street");
        openRequest.setInitialBalance(BigDecimal.valueOf(5000.00));

        when(accountService.openAccount(anyLong(), anyString(), anyString(), anyString(), anyString(), any(BigDecimal.class)))
                .thenReturn(testAccount);

        // When & Then
        mockMvc.perform(post("/api/accounts/open")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACCT1234567890"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));

        verify(accountService).openAccount(eq(1L), eq("SAVINGS"), eq("MVB0001001"), eq("Main Branch"), eq("123 Main Street"), any(BigDecimal.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testGetPendingRequests_Success() throws Exception {
        // Given
        List<AccountRequest> pendingRequests = Arrays.asList(testAccountRequest);
        when(accountRequestService.getPendingRequests()).thenReturn(pendingRequests);

        // When & Then
        mockMvc.perform(get("/api/accounts/requests/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(accountRequestService).getPendingRequests();
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testApproveAccountRequest_Success() throws Exception {
        // Given
        Long requestId = 1L;
        AccountController.ApprovalRequest approvalRequest = new AccountController.ApprovalRequest();
        approvalRequest.setEmployeeId(3L);
        approvalRequest.setComments("Approved after verification");

        AccountRequest approvedRequest = new AccountRequest();
        approvedRequest.setId(requestId);
        approvedRequest.setStatus("APPROVED");
        approvedRequest.setEmployeeComments("Approved after verification");

        when(accountRequestService.approveRequest(anyLong(), anyLong(), anyString()))
                .thenReturn(approvedRequest);

        // When & Then
        mockMvc.perform(post("/api/accounts/requests/{requestId}/approve", requestId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approvalRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.employeeComments").value("Approved after verification"));

        verify(accountRequestService).approveRequest(requestId, 3L, "Approved after verification");
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testRejectAccountRequest_Success() throws Exception {
        // Given
        Long requestId = 1L;
        AccountController.ApprovalRequest rejectionRequest = new AccountController.ApprovalRequest();
        rejectionRequest.setEmployeeId(3L);
        rejectionRequest.setComments("Rejected - incomplete documents");

        AccountRequest rejectedRequest = new AccountRequest();
        rejectedRequest.setId(requestId);
        rejectedRequest.setStatus("REJECTED");
        rejectedRequest.setEmployeeComments("Rejected - incomplete documents");

        when(accountRequestService.rejectRequest(anyLong(), anyLong(), anyString()))
                .thenReturn(rejectedRequest);

        // When & Then
        mockMvc.perform(post("/api/accounts/requests/{requestId}/reject", requestId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rejectionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.employeeComments").value("Rejected - incomplete documents"));

        verify(accountRequestService).rejectRequest(requestId, 3L, "Rejected - incomplete documents");
    }

    @Test
    @WithMockUser(roles = "USER")
    void testSubmitAccountRequest_Exception() throws Exception {
        // Given
        AccountController.AccountOpenRequestDto requestDto = new AccountController.AccountOpenRequestDto();
        requestDto.setUserId(1L);
        requestDto.setAccountType("SAVINGS");
        requestDto.setIfscCode("INVALID_IFSC");
        requestDto.setBranchName("Main Branch");
        requestDto.setBranchAddress("123 Main Street");

        when(accountRequestService.submitAccountRequest(anyLong(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid IFSC code"));

        // When & Then
        mockMvc.perform(post("/api/accounts/request")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(accountRequestService).submitAccountRequest(anyLong(), anyString(), anyString(), anyString(), anyString());
    }
}

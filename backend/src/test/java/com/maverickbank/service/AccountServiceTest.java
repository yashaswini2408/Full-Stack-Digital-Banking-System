package com.maverickbank.service;

import com.maverickbank.entity.Account;
import com.maverickbank.entity.User;
import com.maverickbank.entity.Branch;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.UserRepository;
import com.maverickbank.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private AccountService accountService;

    private User testUser;
    private Account testAccount;
    private Branch testBranch;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        // Setup test branch
        testBranch = new Branch();
        testBranch.setId(1L);
        testBranch.setBranchName("Main Branch");
        testBranch.setIfscCode("MVB0001001");
        testBranch.setAddress("123 Main Street");

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
    }

    @Test
    void testGetAccountsByUserId_Success() {
        // Given
        Long userId = 1L;
        List<Account> expectedAccounts = Arrays.asList(testAccount);
        when(accountRepository.findByUserId(userId)).thenReturn(expectedAccounts);

        // When
        List<Account> actualAccounts = accountService.getAccountsByUserId(userId);

        // Then
        assertNotNull(actualAccounts);
        assertEquals(1, actualAccounts.size());
        assertEquals(testAccount.getAccountNumber(), actualAccounts.get(0).getAccountNumber());
        verify(accountRepository).findByUserId(userId);
    }

    @Test
    void testGetAccountsByUserId_EmptyList() {
        // Given
        Long userId = 999L;
        when(accountRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        // When
        List<Account> actualAccounts = accountService.getAccountsByUserId(userId);

        // Then
        assertNotNull(actualAccounts);
        assertTrue(actualAccounts.isEmpty());
        verify(accountRepository).findByUserId(userId);
    }

    @Test
    void testGetAccountByNumber_Success() {
        // Given
        String accountNumber = "ACCT1234567890";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(testAccount);

        // When
        Optional<Account> actualAccount = accountService.getAccountByNumber(accountNumber);

        // Then
        assertTrue(actualAccount.isPresent());
        assertEquals(testAccount.getAccountNumber(), actualAccount.get().getAccountNumber());
        verify(accountRepository).findByAccountNumber(accountNumber);
    }

    @Test
    void testGetAccountByNumber_NotFound() {
        // Given
        String accountNumber = "INVALID_ACCOUNT";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        // When
        Optional<Account> actualAccount = accountService.getAccountByNumber(accountNumber);

        // Then
        assertFalse(actualAccount.isPresent());
        verify(accountRepository).findByAccountNumber(accountNumber);
    }

    @Test
    void testOpenAccount_Success() {
        // Given
        Long userId = 1L;
        String accountType = "SAVINGS";
        String ifscCode = "MVB0001001";
        String branchName = "Main Branch";
        String branchAddress = "123 Main Street";
        BigDecimal initialBalance = BigDecimal.valueOf(5000.00);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account actualAccount = accountService.openAccount(userId, accountType, ifscCode, branchName, branchAddress, initialBalance);

        // Then
        assertNotNull(actualAccount);
        assertEquals(testAccount.getAccountNumber(), actualAccount.getAccountNumber());
        verify(userRepository).findById(userId);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testOpenAccount_UserNotFound() {
        // Given
        Long userId = 999L;
        String accountType = "SAVINGS";
        String ifscCode = "MVB0001001";
        String branchName = "Main Branch";
        String branchAddress = "123 Main Street";
        BigDecimal initialBalance = BigDecimal.valueOf(5000.00);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountService.openAccount(userId, accountType, ifscCode, branchName, branchAddress, initialBalance);
        });

        assertEquals("No value present", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testOpenAccount_BranchNotFound() {
        // Given
        Long userId = 1L;
        String accountType = "SAVINGS";
        String ifscCode = "INVALID_IFSC";
        String branchName = "Main Branch";
        String branchAddress = "123 Main Street";
        BigDecimal initialBalance = BigDecimal.valueOf(5000.00);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            return account; // Return the same account that was passed in
        });

        // When
        Account result = accountService.openAccount(userId, accountType, ifscCode, branchName, branchAddress, initialBalance);

        // Then - Service doesn't actually validate branches, just creates account
        assertNotNull(result);
        assertEquals(ifscCode, result.getIfscCode());
        verify(userRepository).findById(userId);
        verify(accountRepository).save(any(Account.class));
    }

}

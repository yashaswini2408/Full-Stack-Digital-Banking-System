package com.maverickbank.service;

import com.maverickbank.entity.Bank;
import com.maverickbank.entity.Branch;
import com.maverickbank.repository.BankRepository;
import com.maverickbank.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @Mock
    private BankRepository bankRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BankService bankService;

    private Bank testBank;
    private Branch testBranch1;
    private Branch testBranch2;

    @BeforeEach
    void setUp() {
        // Setup test bank
        testBank = new Bank();
        testBank.setId(1L);
        testBank.setBankName("Maverick Bank");
        testBank.setBankCode("MVB");

        // Setup test branches
        testBranch1 = new Branch();
        testBranch1.setId(1L);
        testBranch1.setBranchName("Main Branch");
        testBranch1.setBranchCode("MAIN001");
        testBranch1.setIfscCode("MVB0001001");
        testBranch1.setAddress("123 Main Street");
        testBranch1.setCity("Mumbai");
        testBranch1.setState("Maharashtra");
        testBranch1.setPincode("400001");
        testBranch1.setBank(testBank);

        testBranch2 = new Branch();
        testBranch2.setId(2L);
        testBranch2.setBranchName("Central Branch");
        testBranch2.setBranchCode("CENT002");
        testBranch2.setIfscCode("MVB0001002");
        testBranch2.setAddress("456 Central Plaza");
        testBranch2.setCity("Delhi");
        testBranch2.setState("Delhi");
        testBranch2.setPincode("110001");
        testBranch2.setBank(testBank);
    }

    @Test
    void testGetAllBanks_Success() {
        // Given
        List<Bank> expectedBanks = Arrays.asList(testBank);
        when(bankRepository.findAll()).thenReturn(expectedBanks);

        // When
        List<Bank> actualBanks = bankService.getAllBanks();

        // Then
        assertNotNull(actualBanks);
        assertEquals(1, actualBanks.size());
        assertEquals("Maverick Bank", actualBanks.get(0).getBankName());
        verify(bankRepository).findAll();
    }

    @Test
    void testGetAllBanks_EmptyList() {
        // Given
        when(bankRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Bank> actualBanks = bankService.getAllBanks();

        // Then
        assertNotNull(actualBanks);
        assertTrue(actualBanks.isEmpty());
        verify(bankRepository).findAll();
    }

    @Test
    void testGetBankById_Success() {
        // Given
        Long bankId = 1L;
        when(bankRepository.findById(bankId)).thenReturn(Optional.of(testBank));

        // When
        Optional<Bank> actualBank = bankService.getBankById(bankId);

        // Then
        assertTrue(actualBank.isPresent());
        assertEquals("Maverick Bank", actualBank.get().getBankName());
        assertEquals("MVB", actualBank.get().getBankCode());
        verify(bankRepository).findById(bankId);
    }

    @Test
    void testGetBankById_NotFound() {
        // Given
        Long bankId = 999L;
        when(bankRepository.findById(bankId)).thenReturn(Optional.empty());

        // When
        Optional<Bank> actualBank = bankService.getBankById(bankId);

        // Then
        assertFalse(actualBank.isPresent());
        verify(bankRepository).findById(bankId);
    }

    @Test
    void testGetBranchesByBankName_Success() {
        // Given
        String bankName = "Maverick Bank";
        List<Branch> expectedBranches = Arrays.asList(testBranch1, testBranch2);
        when(branchRepository.findByBankBankName(bankName)).thenReturn(expectedBranches);

        // When
        List<Branch> actualBranches = bankService.getBranchesByBankName(bankName);

        // Then
        assertNotNull(actualBranches);
        assertEquals(2, actualBranches.size());
        assertEquals("Main Branch", actualBranches.get(0).getBranchName());
        assertEquals("Central Branch", actualBranches.get(1).getBranchName());
        verify(branchRepository).findByBankBankName(bankName);
    }

    @Test
    void testGetBranchesByBankName_EmptyList() {
        // Given
        String bankName = "Non-existent Bank";
        when(branchRepository.findByBankBankName(bankName)).thenReturn(Arrays.asList());

        // When
        List<Branch> actualBranches = bankService.getBranchesByBankName(bankName);

        // Then
        assertNotNull(actualBranches);
        assertTrue(actualBranches.isEmpty());
        verify(branchRepository).findByBankBankName(bankName);
    }

    @Test
    void testGetBranchByIfsc_Success() {
        // Given
        String ifscCode = "MVB0001001";
        when(branchRepository.findByIfscCode(ifscCode)).thenReturn(Optional.of(testBranch1));

        // When
        Optional<Branch> actualBranch = bankService.getBranchByIfsc(ifscCode);

        // Then
        assertTrue(actualBranch.isPresent());
        assertEquals("Main Branch", actualBranch.get().getBranchName());
        assertEquals("MVB0001001", actualBranch.get().getIfscCode());
        assertEquals("Mumbai", actualBranch.get().getCity());
        verify(branchRepository).findByIfscCode(ifscCode);
    }

    @Test
    void testGetBranchByIfsc_NotFound() {
        // Given
        String ifscCode = "INVALID001";
        when(branchRepository.findByIfscCode(ifscCode)).thenReturn(Optional.empty());

        // When
        Optional<Branch> actualBranch = bankService.getBranchByIfsc(ifscCode);

        // Then
        assertFalse(actualBranch.isPresent());
        verify(branchRepository).findByIfscCode(ifscCode);
    }

    @Test
    void testGetAllBranches_Success() {
        // Given
        List<Branch> expectedBranches = Arrays.asList(testBranch1, testBranch2);
        when(branchRepository.findAll()).thenReturn(expectedBranches);

        // When
        List<Branch> actualBranches = bankService.getAllBranches();

        // Then
        assertNotNull(actualBranches);
        assertEquals(2, actualBranches.size());
        verify(branchRepository).findAll();
    }

    @Test
    void testSaveBank_Success() {
        // Given
        Bank newBank = new Bank();
        newBank.setBankName("New Bank");
        newBank.setBankCode("NEW");
        
        when(bankRepository.save(any(Bank.class))).thenReturn(newBank);

        // When
        Bank savedBank = bankService.saveBank(newBank);

        // Then
        assertNotNull(savedBank);
        assertEquals("New Bank", savedBank.getBankName());
        assertEquals("NEW", savedBank.getBankCode());
        verify(bankRepository).save(newBank);
    }

    @Test
    void testSaveBranch_Success() {
        // Given
        Branch newBranch = new Branch();
        newBranch.setBranchName("New Branch");
        newBranch.setIfscCode("MVB0001003");
        
        when(branchRepository.save(any(Branch.class))).thenReturn(newBranch);

        // When
        Branch savedBranch = bankService.saveBranch(newBranch);

        // Then
        assertNotNull(savedBranch);
        assertEquals("New Branch", savedBranch.getBranchName());
        assertEquals("MVB0001003", savedBranch.getIfscCode());
        verify(branchRepository).save(newBranch);
    }

    @Test
    void testSaveBank_NullInput() {
        // When
        when(bankRepository.save(null)).thenReturn(null);
        Bank result = bankService.saveBank(null);

        // Then - Service doesn't validate null, just passes to repository
        assertNull(result);
        verify(bankRepository).save(null);
    }

    @Test
    void testSaveBranch_NullInput() {
        // When
        when(branchRepository.save(null)).thenReturn(null);
        Branch result = bankService.saveBranch(null);

        // Then - Service doesn't validate null, just passes to repository
        assertNull(result);
        verify(branchRepository).save(null);
    }
}

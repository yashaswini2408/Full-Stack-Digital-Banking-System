package com.maverickbank.controller;

import com.maverickbank.entity.Bank;
import com.maverickbank.entity.Branch;
import com.maverickbank.service.BankService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser
    void testGetAllBanks_Success() throws Exception {
        // Given
        List<Bank> banks = Arrays.asList(testBank);
        when(bankService.getAllBanks()).thenReturn(banks);

        // When & Then
        mockMvc.perform(get("/api/banks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].bankName").value("Maverick Bank"))
                .andExpect(jsonPath("$[0].bankCode").value("MVB"));

        verify(bankService).getAllBanks();
    }

    @Test
    @WithMockUser
    void testGetBankById_Success() throws Exception {
        // Given
        Long bankId = 1L;
        when(bankService.getBankById(bankId)).thenReturn(Optional.of(testBank));

        // When & Then
        mockMvc.perform(get("/api/banks/{id}", bankId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bankName").value("Maverick Bank"))
                .andExpect(jsonPath("$.bankCode").value("MVB"));

        verify(bankService).getBankById(bankId);
    }

    @Test
    @WithMockUser
    void testGetBankById_NotFound() throws Exception {
        // Given
        Long bankId = 999L;
        when(bankService.getBankById(bankId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/banks/{id}", bankId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bankService).getBankById(bankId);
    }

    @Test
    @WithMockUser
    void testGetBranchesByBankName_Success() throws Exception {
        // Given
        String bankName = "Maverick Bank";
        List<Branch> branches = Arrays.asList(testBranch1, testBranch2);
        when(bankService.getBranchesByBankName(bankName)).thenReturn(branches);

        // When & Then
        mockMvc.perform(get("/api/banks/{bankName}/branches", bankName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].branchName").value("Main Branch"))
                .andExpect(jsonPath("$[0].ifscCode").value("MVB0001001"))
                .andExpect(jsonPath("$[1].branchName").value("Central Branch"))
                .andExpect(jsonPath("$[1].ifscCode").value("MVB0001002"));

        verify(bankService).getBranchesByBankName(bankName);
    }

    @Test
    @WithMockUser
    void testGetBranchesByBankName_EmptyList() throws Exception {
        // Given
        String bankName = "Non-existent Bank";
        when(bankService.getBranchesByBankName(bankName)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/banks/{bankName}/branches", bankName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(bankService).getBranchesByBankName(bankName);
    }

    @Test
    @WithMockUser
    void testGetBranchByIfsc_Success() throws Exception {
        // Given
        String ifscCode = "MVB0001001";
        when(bankService.getBranchByIfsc(ifscCode)).thenReturn(Optional.of(testBranch1));

        // When & Then
        mockMvc.perform(get("/api/banks/branches/ifsc/{ifscCode}", ifscCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchName").value("Main Branch"))
                .andExpect(jsonPath("$.ifscCode").value("MVB0001001"))
                .andExpect(jsonPath("$.city").value("Mumbai"))
                .andExpect(jsonPath("$.state").value("Maharashtra"));

        verify(bankService).getBranchByIfsc(ifscCode);
    }

    @Test
    @WithMockUser
    void testGetBranchByIfsc_NotFound() throws Exception {
        // Given
        String ifscCode = "INVALID001";
        when(bankService.getBranchByIfsc(ifscCode)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/banks/branches/ifsc/{ifscCode}", ifscCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bankService).getBranchByIfsc(ifscCode);
    }

    @Test
    @WithMockUser
    void testGetAllBranches_Success() throws Exception {
        // Given
        List<Branch> branches = Arrays.asList(testBranch1, testBranch2);
        when(bankService.getAllBranches()).thenReturn(branches);

        // When & Then
        mockMvc.perform(get("/api/banks/branches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].branchName").value("Main Branch"))
                .andExpect(jsonPath("$[1].branchName").value("Central Branch"));

        verify(bankService).getAllBranches();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateBank_Success() throws Exception {
        // Given
        Bank newBank = new Bank();
        newBank.setBankName("New Test Bank");
        newBank.setBankCode("NTB");

        Bank savedBank = new Bank();
        savedBank.setId(2L);
        savedBank.setBankName("New Test Bank");
        savedBank.setBankCode("NTB");

        when(bankService.saveBank(any(Bank.class))).thenReturn(savedBank);

        // When & Then
        mockMvc.perform(post("/api/banks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBank)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.bankName").value("New Test Bank"))
                .andExpect(jsonPath("$.bankCode").value("NTB"));

        verify(bankService).saveBank(any(Bank.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateBranch_Success() throws Exception {
        // Given
        Branch newBranch = new Branch();
        newBranch.setBranchName("New Test Branch");
        newBranch.setIfscCode("MVB0001003");
        newBranch.setAddress("789 Test Street");
        newBranch.setCity("Pune");
        newBranch.setState("Maharashtra");
        newBranch.setPincode("411001");

        Branch savedBranch = new Branch();
        savedBranch.setId(3L);
        savedBranch.setBranchName("New Test Branch");
        savedBranch.setIfscCode("MVB0001003");
        savedBranch.setAddress("789 Test Street");
        savedBranch.setCity("Pune");
        savedBranch.setState("Maharashtra");
        savedBranch.setPincode("411001");

        when(bankService.saveBranch(any(Branch.class))).thenReturn(savedBranch);

        // When & Then
        mockMvc.perform(post("/api/banks/branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBranch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.branchName").value("New Test Branch"))
                .andExpect(jsonPath("$.ifscCode").value("MVB0001003"))
                .andExpect(jsonPath("$.city").value("Pune"));

        verify(bankService).saveBranch(any(Branch.class));
    }

    @Test
    @WithMockUser
    void testGetAllBanks_EmptyList() throws Exception {
        // Given
        when(bankService.getAllBanks()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/banks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(bankService).getAllBanks();
    }

    @Test
    @WithMockUser
    void testGetBranchesByBankName_SpecialCharacters() throws Exception {
        // Given
        String bankName = "State Bank of India";
        List<Branch> branches = Arrays.asList(testBranch1);
        when(bankService.getBranchesByBankName(bankName)).thenReturn(branches);

        // When & Then
        mockMvc.perform(get("/api/banks/{bankName}/branches", bankName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].branchName").value("Main Branch"));

        verify(bankService).getBranchesByBankName(bankName);
    }
}

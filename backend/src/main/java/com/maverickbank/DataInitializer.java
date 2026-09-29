package com.maverickbank;

import com.maverickbank.entity.*;
import com.maverickbank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.util.Set;

@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initializeData(
            RoleRepository roleRepository, 
            UserRepository userRepository,
            BankRepository bankRepository,
            BranchRepository branchRepository,
            LoanTypeRepository loanTypeRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Initialize Roles
            if (roleRepository.findByName("CUSTOMER").isEmpty())
                roleRepository.save(new Role(null, "CUSTOMER"));
            if (roleRepository.findByName("EMPLOYEE").isEmpty())
                roleRepository.save(new Role(null, "EMPLOYEE"));
            if (roleRepository.findByName("ADMIN").isEmpty())
                roleRepository.save(new Role(null, "ADMIN"));

            // Initialize Admin User
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.findByName("ADMIN").get();
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setName("Administrator");
                admin.setEmail("admin@maverickbank.com");
                admin.setContactNumber("0000000000");
                admin.setAddress("HQ");
                admin.setRoles(Set.of(adminRole));
                admin.setIsActive(true);
                userRepository.save(admin);
            }

            // Initialize Sample Employee
            if (userRepository.findByUsername("employee1").isEmpty()) {
                Role employeeRole = roleRepository.findByName("EMPLOYEE").get();
                User employee = new User();
                employee.setUsername("employee1");
                employee.setPassword(passwordEncoder.encode("emp123"));
                employee.setName("John Employee");
                employee.setEmail("employee@maverickbank.com");
                employee.setContactNumber("1111111111");
                employee.setAddress("Branch Office");
                employee.setRoles(Set.of(employeeRole));
                employee.setIsActive(true);
                userRepository.save(employee);
            }

            // Initialize Banks
            if (bankRepository.findByBankName("Maverick Bank").isEmpty()) {
                Bank maverickBank = new Bank();
                maverickBank.setBankName("Maverick Bank");
                maverickBank.setBankCode("MVB");
                bankRepository.save(maverickBank);

                // Initialize Branches for Maverick Bank
                if (branchRepository.findByIfscCode("MVB0001001").isEmpty()) {
                    Branch mainBranch = new Branch();
                    mainBranch.setBranchName("Main Branch");
                    mainBranch.setBranchCode("MAIN001");
                    mainBranch.setIfscCode("MVB0001001");
                    mainBranch.setAddress("123 Main Street");
                    mainBranch.setCity("Mumbai");
                    mainBranch.setState("Maharashtra");
                    mainBranch.setPincode("400001");
                    mainBranch.setBank(maverickBank);
                    branchRepository.save(mainBranch);
                }

                if (branchRepository.findByIfscCode("MVB0001002").isEmpty()) {
                    Branch centralBranch = new Branch();
                    centralBranch.setBranchName("Central Branch");
                    centralBranch.setBranchCode("CENT002");
                    centralBranch.setIfscCode("MVB0001002");
                    centralBranch.setAddress("456 Central Plaza");
                    centralBranch.setCity("Delhi");
                    centralBranch.setState("Delhi");
                    centralBranch.setPincode("110001");
                    centralBranch.setBank(maverickBank);
                    branchRepository.save(centralBranch);
                }
            }

            // Initialize other major banks
            if (bankRepository.findByBankName("State Bank of India").isEmpty()) {
                Bank sbi = new Bank();
                sbi.setBankName("State Bank of India");
                sbi.setBankCode("SBI");
                bankRepository.save(sbi);

                Branch sbiBranch = new Branch();
                sbiBranch.setBranchName("SBI Main Branch");
                sbiBranch.setBranchCode("SBI001");
                sbiBranch.setIfscCode("SBIN0000001");
                sbiBranch.setAddress("SBI Building");
                sbiBranch.setCity("Mumbai");
                sbiBranch.setState("Maharashtra");
                sbiBranch.setPincode("400002");
                sbiBranch.setBank(sbi);
                branchRepository.save(sbiBranch);
            }

            // Initialize Loan Types
            if (loanTypeRepository.findAll().isEmpty()) {
                // Personal Loan
                LoanType personalLoan = new LoanType(
                    "Personal Loan", 
                    12.5, 
                    60, 
                    new BigDecimal("1000000"), 
                    "Unsecured personal loan for any purpose"
                );
                loanTypeRepository.save(personalLoan);

                // Home Loan
                LoanType homeLoan = new LoanType(
                    "Home Loan", 
                    8.5, 
                    360, 
                    new BigDecimal("10000000"), 
                    "Secured loan for purchasing or constructing a house"
                );
                loanTypeRepository.save(homeLoan);

                // Car Loan
                LoanType carLoan = new LoanType(
                    "Car Loan", 
                    9.0, 
                    84, 
                    new BigDecimal("2000000"), 
                    "Secured loan for purchasing a vehicle"
                );
                loanTypeRepository.save(carLoan);

                // Education Loan
                LoanType educationLoan = new LoanType(
                    "Education Loan", 
                    7.5, 
                    180, 
                    new BigDecimal("2500000"), 
                    "Loan for higher education expenses"
                );
                loanTypeRepository.save(educationLoan);

                // Business Loan
                LoanType businessLoan = new LoanType(
                    "Business Loan", 
                    14.0, 
                    120, 
                    new BigDecimal("5000000"), 
                    "Loan for business expansion and working capital"
                );
                loanTypeRepository.save(businessLoan);
            }
        };
    }
}

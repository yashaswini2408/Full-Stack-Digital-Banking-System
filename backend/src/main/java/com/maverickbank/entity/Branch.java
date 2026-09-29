package com.maverickbank.entity;

import jakarta.persistence.*;

@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String branchName;

    @Column(nullable = false, unique = true)
    private String branchCode;

    @Column(nullable = false, unique = true)
    private String ifscCode;

    @Column(nullable = false)
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public Branch() {}

    public Branch(Long id, String branchName, String branchCode, String ifscCode, String address, String city, String state, String pincode, Bank bank) {
        this.id = id;
        this.branchName = branchName;
        this.branchCode = branchCode;
        this.ifscCode = ifscCode;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.bank = bank;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }
}

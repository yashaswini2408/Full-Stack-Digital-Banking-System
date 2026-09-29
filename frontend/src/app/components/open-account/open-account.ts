import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AccountService } from '../../services/account.service';
import { CustomerService } from '../../services/customer.service';
import { BankBranchService } from '../../services/bank-branch.service';
import { Account } from '../../models/account';
import { BankBranch } from '../../models/bank-branch';

@Component({
  selector: 'app-open-account',
  standalone: false,
  templateUrl: './open-account.html',
  styleUrls: ['./open-account.css']
})
export class OpenAccount implements OnInit {
  account: Account = {
    customerId: null,
    branchId: 0,
    accountType: '',
    balance: 0,
    ifscCode: ''
  };

  bankBranches: BankBranch[] = [];

  success = false;
  error = '';
  inputUserId: number = 0; // 🆕 Added for manual input/testing purpose

  constructor(
    private accountService: AccountService,
    private customerService: CustomerService,
    private bankBranchService: BankBranchService
  ) {}

  ngOnInit(): void {
    // 🆕 Removed authService. You can set customerId manually via input field (demo mode).
    this.fetchBranches();
  }

  fetchBranches() {
    this.bankBranchService.getAllBankBranches().subscribe({
      next: (branches) => {
        this.bankBranches = branches;
      },
      error: () => {
        this.error = 'Failed to load bank branches';
      }
    });
  }

  // 🆕 Called before account creation to fetch customerId using entered userId
  fetchCustomerAndSubmit(form: NgForm) {
    if (!this.inputUserId) {
      this.error = 'Please enter a valid User ID';
      return;
    }

    this.customerService.getCustomerByUserId(this.inputUserId).subscribe({
      next: (res) => {
        this.account.customerId = res?.customerId ?? null;
        if (this.account.customerId) {
          this.createAccount(form);
        } else {
          this.error = 'Customer not found';
        }
      },
      error: () => {
        this.error = 'Customer lookup failed';
      }
    });
  }

  createAccount(form: NgForm) {
    this.accountService.openAccount(this.account).subscribe({
      next: () => {
        this.success = true;
        this.error = '';
        form.resetForm({
          branchId: 0,
          accountType: '',
          balance: 0,
          ifscCode: '',
          customerId: null
        });
      },
      error: () => {
        this.success = false;
        this.error = 'Account creation failed';
      }
    });
  }

  onSubmit(form: NgForm) {
  if (form.valid) {
    this.accountService.openAccount(this.account).subscribe({
      next: () => {
        this.success = true;
        this.error = '';
        form.resetForm({
          branchId: 0,
          accountType: '',
          balance: 0,
          ifscCode: '',
          customerId: this.account.customerId
        });
      },
      error: () => {
        this.success = false;
        this.error = 'Account creation failed';
      }
    });
  }
}

}

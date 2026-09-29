import { Component, OnInit } from '@angular/core';
import { Account } from '../../models/account';
import { AccountService } from '../../services/account.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-my-accounts',
  standalone: false,
  templateUrl: './my-accounts.html',
  styleUrl: './my-accounts.css'
})
export class MyAccounts implements OnInit {

  customerId: number | null = null;
  accounts: Account[] = [];
  submitted = false;
  errorMessage = '';

  constructor(
    private accountService: AccountService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('customerId');
      if (id) {
        this.customerId = +id;  // Convert string to number
        this.loadAccounts();
      } else {
        this.errorMessage = 'Customer ID not provided in URL';
      }
    });
  }

  loadAccounts() {
    this.errorMessage = '';
    if (this.customerId === null) {
      this.errorMessage = 'Customer ID is null';
      return;
    }

    this.accountService.getAccountsByCustomerId(this.customerId).subscribe({
      next: (data) => this.accounts = data,
      error: () => this.errorMessage = 'Error fetching accounts'
    });
  }

  onSubmit() {
    this.submitted = true;
    this.errorMessage = '';

    if (this.customerId === null) {
      this.errorMessage = 'Customer ID is required';
      return;
    }

    this.accountService.getAccountsByCustomerId(this.customerId).subscribe({
      next: (data) => this.accounts = data,
      error: () => this.errorMessage = 'Error fetching accounts'
    });
  }

  // You can add navigation methods here if needed
}
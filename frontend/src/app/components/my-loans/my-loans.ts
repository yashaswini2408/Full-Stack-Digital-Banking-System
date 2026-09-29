import { Component, OnInit } from '@angular/core';
import { LoanService } from '../../services/loan.service';
import { LoanApplication } from '../../models/loan-application';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-my-loans',
  standalone: false,
  templateUrl: './my-loans.html',
  styleUrl: './my-loans.css'
})
export class MyLoans implements OnInit {
  customerId: number | null = null;
  loanApplications: LoanApplication[] = [];
  message: string = '';
  messageClass: string = '';

  constructor(
    private loanService: LoanService,
    private route: ActivatedRoute
  ) {}

ngOnInit(): void {
  const id = this.route.snapshot.paramMap.get('customerId');
    this.customerId = id ? +id : null;
  if (this.customerId) {
    this.loadLoanApplications();
  } else {
    this.message = 'Customer ID not found. Please log in.';
    this.messageClass = 'alert alert-danger';
  }
  }

  loadLoanApplications(): void {
    this.loanService.getLoanApplicationsByCustomerId(this.customerId!).subscribe({
      next: (data) => {
        this.loanApplications = data;
        if (data.length === 0) {
          this.message = 'No loan applications found for this customer.';
          this.messageClass = 'alert alert-info';
        } else {
          this.message = '';
        }
      },
      error: (err) => {
        this.message = 'Error loading loan applications.';
        this.messageClass = 'alert alert-danger';
      }
    });
  }
}
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoanService } from '../../services/loan.service';
import { Loan } from '../../models/loan';

@Component({
  selector: 'app-view-loans',
  standalone: false,
  templateUrl: './view-loans.html',
  styleUrl: './view-loans.css'
})
export class ViewLoans implements OnInit {
  loans: Loan[] = [];

  constructor(private loanService: LoanService, private router: Router) {}

  ngOnInit(): void {
    this.loanService.getAllLoans().subscribe(data => this.loans = data);
  }

  onApplyLoan(loan: Loan): void {
    this.router.navigate(['/customer-dashboard/apply-loan'], {
      queryParams: { loanId: loan.loanId }
    });
  }
}
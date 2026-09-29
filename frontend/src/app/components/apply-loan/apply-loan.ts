import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { LoanService } from '../../services/loan.service';
import { LoanApplication } from '../../models/loan-application';

@Component({
  selector: 'app-apply-loan',
  standalone: false,
  templateUrl: './apply-loan.html',
  styleUrl: './apply-loan.css'
})
export class ApplyLoan implements OnInit {

  loanId!: number;
  customerId!: number;
  requestedAmount: number | null = null;
  purpose: string = '';

  message = '';
  messageClass = '';

  constructor(private route: ActivatedRoute, private loanService: LoanService, private router: Router) {}

  ngOnInit(): void {
    this.loanId = +this.route.snapshot.queryParamMap.get('loanId')!;
  }

  onApplyLoan(form: NgForm): void {
    if (form.invalid) return;

    const application: LoanApplication = {
      customerId: this.customerId,
      loanId: this.loanId,
      requestedAmount: this.requestedAmount!,
      purpose: this.purpose
    };

    this.loanService.applyLoan(application).subscribe({
      next: () => {
        this.message = 'Loan application submitted successfully!';
        this.messageClass = 'alert alert-success';
        form.resetForm();
      },
      error: () => {
        this.message = 'Application submission failed!';
        this.messageClass = 'alert alert-danger';
      }
    });
  }

}

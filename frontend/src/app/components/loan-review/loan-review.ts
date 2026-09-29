import { Component, OnInit } from '@angular/core';
import { LoanApplication } from '../../models/loan-application';
import { LoanService } from '../../services/loan.service';

@Component({
  selector: 'app-loan-review',
  standalone: false,
  templateUrl: './loan-review.html',
  styleUrl: './loan-review.css'
})
export class LoanReview implements OnInit {
  loanApplications: LoanApplication[] = [];
  editMode: number | null = null;
  editedStatus: string = '';

  constructor(private loanService: LoanService) {}

  ngOnInit(): void {
    this.fetchLoanApplications();
  }

  fetchLoanApplications() {
    this.loanService.getAllLoanApplications().subscribe(data => {
      this.loanApplications = data;
    });
  }

  enableEdit(applicationId: number, currentStatus: string) {
    this.editMode = applicationId;
    this.editedStatus = currentStatus;
  }

  updateStatus(applicationId: number) {
    this.loanService.updateLoanApplication(applicationId, { status: this.editedStatus })
      .subscribe(() => {
        this.editMode = null;
        this.fetchLoanApplications();
      });
  }

  cancelEdit() {
    this.editMode = null;
  }
}
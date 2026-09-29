import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-transactions',
  standalone: false,
  templateUrl: './transactions.html',
  styleUrl: './transactions.css'
})
export class Transactions {

  operation: 'deposit' | 'withdraw' | 'transfer' = 'deposit';

  depositAccountId = '';
  depositAmount: number | null = null;

  withdrawAccountId = '';
  withdrawAmount: number | null = null;

  transferFromAccountId = '';
  transferToAccountId = '';
  transferAmount: number | null = null;

  message = '';
  messageClass = '';
  loading = false;

  constructor(private transactionService: TransactionService) {}

  switchOperation(op: 'deposit' | 'withdraw' | 'transfer') {
    this.operation = op;
    this.message = '';
    this.messageClass = '';
    this.loading = false;
    this.resetFormFields();
  }

  resetFormFields() {
    this.depositAccountId = '';
    this.depositAmount = null;
    this.withdrawAccountId = '';
    this.withdrawAmount = null;
    this.transferFromAccountId = '';
    this.transferToAccountId = '';
    this.transferAmount = null;
  }

  onDeposit(form: NgForm) {
  if (!form.valid) return;
  this.loading = true;
  this.message = '';
  this.transactionService.deposit(+this.depositAccountId, this.depositAmount!).subscribe({
    next: () => {
      this.message = 'Deposit successful!';
      this.messageClass = 'alert-success';
      form.resetForm();
      this.loading = false;
    },
    error: () => {
      this.message = 'Deposit failed. Please try again.';
      this.messageClass = 'alert-danger';
      this.loading = false;
    },
  });
}

onWithdraw(form: NgForm) {
  if (!form.valid) return;
  this.loading = true;
  this.message = '';
  this.transactionService.withdraw(+this.withdrawAccountId, this.withdrawAmount!).subscribe({
    next: () => {
      this.message = 'Withdrawal successful!';
      this.messageClass = 'alert-success';
      form.resetForm();
      this.loading = false;
    },
    error: () => {
      this.message = 'Withdrawal failed. Please try again.';
      this.messageClass = 'alert-danger';
      this.loading = false;
    },
  });
}

onTransfer(form: NgForm) {
  if (!form.valid) return;
  this.loading = true;
  this.message = '';
  this.transactionService.transfer(this.transferFromAccountId, this.transferToAccountId, this.transferAmount!).subscribe({
    next: () => {
      this.message = 'Transfer successful!';
      this.messageClass = 'alert-success';
      form.resetForm();
      this.loading = false;
    },
    error: () => {
      this.message = 'Transfer failed. Please try again.';
      this.messageClass = 'alert-danger';
      this.loading = false;
    },
  });
}

}

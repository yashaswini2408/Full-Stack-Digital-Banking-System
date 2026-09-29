import { Component, OnInit } from '@angular/core';
import { Transaction } from '../../models/transaction';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-transaction-view',
  standalone: false,
  templateUrl: './transaction-view.html',
  styleUrl: './transaction-view.css'
})
export class TransactionView implements OnInit {
  allTransactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];
  accountIdSearch: string = '';

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadAllTransactions();
  }

  loadAllTransactions(): void {
    this.transactionService.getAllTransactions().subscribe(data => {
      this.allTransactions = data;
      this.filteredTransactions = data;
    });
  }

  filterByAccountId(): void {
    if (this.accountIdSearch.trim() === '') {
      this.filteredTransactions = this.allTransactions;
    } else {
      const accountId = parseInt(this.accountIdSearch, 10);
      this.filteredTransactions = this.allTransactions.filter(
        tx => tx.accountId === accountId
      );
    }
  }
}
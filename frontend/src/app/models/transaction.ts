export interface Transaction {
  transactionId?: number;
  accountId?: number;       
  transactionType:'Deposit' | 'Withdraw' | 'Transfer';
  amount: number;
  transactionDate: string; 
}
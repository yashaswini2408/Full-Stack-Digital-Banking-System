export interface Loan {
  loanId?: number;
  loanType: string;
  principalAmount: number;
  interestRate: number;
  tenureMonths: number;
}
export interface LoanApplication {
  applicationId?: number
  customerId?: number;
  loanId: number;
  requestedAmount: number;
  purpose: string;
  applicationDate?: string;
  status?: string;
}
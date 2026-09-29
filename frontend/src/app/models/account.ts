export interface Account {
  accountId?: number;          
  customerId?: number|null;
  branchId: number;
  accountNumber?: string;
  accountType: string;
  balance: number;
  dateOpened?: string;
  ifscCode: string;
}
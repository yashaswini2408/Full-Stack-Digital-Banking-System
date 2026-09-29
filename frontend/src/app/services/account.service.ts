import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../models/account';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private baseUrl = 'http://localhost:8080/api/v1/accounts';

  constructor(private http: HttpClient) {}

  openAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(`${this.baseUrl}/createaccount`, account);
  }

  getAccountsByCustomerId(customerId: number): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  getAllAccounts(): Observable<Account[]> {
  return this.http.get<Account[]>(`${this.baseUrl}/getallaccounts`);
}

  deleteAccount(accountId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/deleteaccount/${accountId}`);
  }
}

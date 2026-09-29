import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BankBranch } from '../models/bank-branch';

@Injectable({
  providedIn: 'root'
})
export class BankBranchService {

  private baseUrl = 'http://localhost:8080/api/v1/branches';

  constructor(private http: HttpClient) {}

  getAllBankBranches(): Observable<BankBranch[]> {
    return this.http.get<BankBranch[]>(`${this.baseUrl}/getAllBankBranches`);
  }
}

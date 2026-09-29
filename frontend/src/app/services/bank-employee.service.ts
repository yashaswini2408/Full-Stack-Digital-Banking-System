import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BankEmployee } from '../models/bank-employee';

@Injectable({
  providedIn: 'root'
})
export class BankEmployeeService {
  
  private baseUrl = 'http://localhost:8080/api/v1/employees';

  constructor(private http: HttpClient) {}

  getAllBankEmployees(): Observable<BankEmployee[]> {
    return this.http.get<BankEmployee[]>(`${this.baseUrl}/getAllBankEmployees`);
  }

  createBankEmployee(employee: BankEmployee): Observable<BankEmployee> {
    return this.http.post<BankEmployee>(`${this.baseUrl}/createBankEmployee`, employee);
  }

  updateBankEmployee(userId: number, employee: BankEmployee): Observable<BankEmployee> {
    return this.http.put<BankEmployee>(`${this.baseUrl}/updateBankEmployee/${userId}`, employee);
  }

  deleteBankEmployee(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/deleteBankEmployee/${userId}`);
  }

  getBankEmployeeByUserId(userId: number): Observable<BankEmployee> {
    return this.http.get<BankEmployee>(`${this.baseUrl}/getBankEmployeeByUserId/${userId}`);
  }

  getBankEmployeeById(employeeId: number): Observable<BankEmployee> {
    return this.http.get<BankEmployee>(`${this.baseUrl}/getBankEmployeeById/${employeeId}`);
  }
}
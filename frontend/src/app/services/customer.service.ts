import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Customer } from '../models/customer';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private baseUrl = 'http://localhost:8080/api/v1/customers';

  constructor(private http: HttpClient) {}

  registerCustomer(customer: Customer): Observable<Customer> {
  const token = localStorage.getItem('token');
  const headers = {
    'Authorization': `Bearer ${token}`
  };

  return this.http.post<Customer>(
    `${this.baseUrl}/createCustomer`,
    customer,
    { headers }
  );
  }


  getCustomerByUserId(userId: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/getCustomerByUserId/${userId}`);
  }

  getCustomerById(customerId: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/getCustomerById/${customerId}`);
  }

  updateCustomer(customerId: number, customer: Customer): Observable<Customer> {
    return this.http.put<Customer>(`${this.baseUrl}/updateCustomer/${customerId}`, customer);
  }

  getAllCustomers(): Observable<Customer[]> {
  return this.http.get<Customer[]>(`${this.baseUrl}/getAllCustomers`);
  }

}

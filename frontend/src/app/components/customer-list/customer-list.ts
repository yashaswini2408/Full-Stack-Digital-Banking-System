import { Component } from '@angular/core';
import { Customer } from '../../models/customer';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-customer-list',
  standalone: false,
  templateUrl: './customer-list.html',
  styleUrl: './customer-list.css'
})
export class CustomerList {

  customers: Customer[] = [];
  filteredCustomers: Customer[] = [];
  searchId: string = '';

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getAllCustomers().subscribe(data => {
      this.customers = data;
      this.filteredCustomers = data;
    });
  }

  filterCustomers(): void {
    if (this.searchId.trim() === '') {
      this.filteredCustomers = this.customers;
    } else {
      const id = parseInt(this.searchId, 10);
      this.filteredCustomers = this.customers.filter(c => c.customerId === id);
    }
  }

}

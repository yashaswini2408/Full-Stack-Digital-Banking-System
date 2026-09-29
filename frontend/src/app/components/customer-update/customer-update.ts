import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-customer-update',
  standalone: false,
  templateUrl: './customer-update.html',
  styleUrl: './customer-update.css'
})
export class CustomerUpdate implements OnInit {

  customer: Customer = {
    customerId: 0,
    userId: 0,
    name: '',
    gender: '',
    contactNumber: '',
    address: '',
    dateOfBirth: '',
    aadharNumber: '',
    panNumber: ''
  };
  updateSuccess = false;
  updateError = '';

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('customerId');
    if (id) {
      const customerId = +id;
      this.customerService.getCustomerById(customerId).subscribe({
        next: (data) => {
          this.customer = data;
        },
        error: () => {
          this.updateError = 'Failed to load customer data';
        }
      });
    } else {
      this.updateError = 'No customer id provided';
    }
  }

onSubmit(form: NgForm): void {
  if (form.valid) {
    if (!this.customer.customerId) {
      this.updateError = 'Customer ID is missing.';
      return;
    }
    this.customerService.updateCustomer(this.customer.customerId, this.customer).subscribe({
      next: () => {
        this.updateSuccess = true;
        this.updateError = '';
this.router.navigate([`/customer-dashboard/customer-view-profile/${this.customer.userId}`]);
      },
      error: () => {
        this.updateError = 'Update failed. Try again.';
        this.updateSuccess = false;
      }
    });
  }
}

}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer';

@Component({
  selector: 'app-customer-view-profile',
  standalone: false,
  templateUrl: './customer-view-profile.html',
  styleUrl: './customer-view-profile.css'
})
export class CustomerViewProfile implements OnInit {

  customer?: Customer;
  userId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService
  ) {}

ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('userId');
    if (idParam) {
      this.userId = +idParam; // Convert to number
      this.customerService.getCustomerByUserId(this.userId).subscribe({
        next: (data) => {
          this.customer = data;
          if (!this.customer) {
            console.warn('No customer found for userId:', this.userId);
          }
        },
        error: (error) => {
          console.error('Error fetching customer:', error);
          this.customer = undefined;
        }
      });
    } else {
      console.warn('No userId provided in path params');
    }
  }

}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { NgForm } from '@angular/forms';
import { Customer } from '../../models/customer';

@Component({
  selector: 'app-customer-register',
  standalone: false,
  templateUrl: './customer-register.html',
  styleUrls: ['./customer-register.css']
})
export class CustomerRegister implements OnInit {

  customer: Customer = {
    userId: 0,
    name: '',
    gender: '',
    contactNumber: '',
    address: '',
    dateOfBirth: '',
    aadharNumber: '',
    panNumber: ''
  };

  storedUserId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUserJson = localStorage.getItem('currentUser');
    if (currentUserJson) {
      const user = JSON.parse(currentUserJson);
      this.storedUserId = user.userId;
      this.customer.userId = user.userId;
      console.log('userId set to:', user.userId);
    }
  }

  onSubmit(form: NgForm) {
    if (form.valid && this.customer.userId === this.storedUserId) {
      this.customerService.registerCustomer(this.customer).subscribe({
        next: () => {
          alert('Registration successful!');
          form.resetForm();
          this.router.navigate(['/customer-dashboard']);
        },
        error: (err) => {
          alert('Registration failed. Try again.');
          console.error(err);
        }
      });
    } else {
      alert('Entered User ID does not match your login ID.');
    }
  }
}
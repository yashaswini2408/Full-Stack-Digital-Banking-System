import { Component, OnInit, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-customer-dashboard',
  standalone: false,
  templateUrl: './customer-dashboard.html',
  styleUrls: ['./customer-dashboard.css']  // ✅ fixed typo: styleUrls instead of styleUrl
})
export class CustomerDashboard implements OnInit {
  isCollapsed = false;
  customer?: Customer;
  userId = 0;
  roleId = 0;
  roleName = '';
  accountId = 0;

  constructor(
    private renderer: Renderer2,
    private customerService: CustomerService,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit() {
  const token = localStorage.getItem('token');
  const userJson = localStorage.getItem('currentUser');

  if (!token || !userJson) {
    this.logout();
    return;
  }

  const user = JSON.parse(userJson);

  if (!user?.userId || !user?.roleId || !user?.roleName) {
    this.logout();
    return;
  }

  this.userId = user.userId;
  this.roleId = user.roleId;
  this.roleName = user.roleName;

  // Check if customer exists
  this.customerService.getCustomerByUserId(this.userId).subscribe({
    next: (data) => {
      this.customer = data;

      if (this.customer?.customerId) {
        this.accountService.getAccountsByCustomerId(this.customer.customerId).subscribe({
          next: (accounts) => {
            if (accounts?.length > 0) {
              this.accountId = accounts[0].accountId!;
            }
          },
          error: (err) => console.error('Failed to fetch accounts:', err)
        });
      }
    },
    error: (err) => {
      if (err.status === 404) {
        // 👇 Redirect to customer registration form
        this.router.navigate(['/customer-register']);
      } else {
        console.error('Failed to load customer:', err);
        this.logout();
      }
    }
  });
  }


  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
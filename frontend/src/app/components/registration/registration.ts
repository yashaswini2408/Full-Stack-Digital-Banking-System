import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from '../../models/user';

@Component({
  selector: 'app-registration',
  standalone: false,
  templateUrl: './registration.html',
  styleUrl: './registration.css'
})
export class Registration {

  user = {
    username: '',
    email: '',
    password: '',
    roleId: null as number | null
  };

  roles = [
    { id: 1, name: 'CUSTOMER' },
    { id: 2, name: 'BANK_EMPLOYEE' },
    { id: 3, name: 'ADMINISTRATOR' }
  ];

  errorMessage = '';
  returnUrl = '';

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  onSubmit(form: NgForm) {
    if (form.invalid || this.user.roleId === null) {
      return;
    }

    const newUser: User = {
      username: this.user.username,
      email: this.user.email,
      password: this.user.password,
      roleId: this.user.roleId
    };

    this.userService.registerUser(newUser).subscribe({
      next: () => {
        // Redirect to login with success flag
        this.router.navigate(['/login'], { queryParams: { registered: 'true' } });
      },
      error: () => this.errorMessage = 'Registration failed. Try again.'
    });
  }

}

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login implements OnInit {
  loginForm!: FormGroup;
  errorMessage = '';
  successMessage = '';
  returnUrl = '/';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', Validators.required]
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    this.route.queryParams.subscribe(params => {
      if (params['registered'] === 'true') {
        this.successMessage = 'Registration successful. Please login.';
      }
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    const { username, password } = this.loginForm.value;

    this.http.post<any>('http://localhost:8080/api/v1/users/login', { username, password })
      .subscribe({
        next: (res) => {
          if (!res?.token || !res?.username || !res?.roleName || !res?.userId) {
            this.errorMessage = 'Invalid response from server.';
            return;
          }

          localStorage.setItem('token', res.token);
          localStorage.setItem('currentUser', JSON.stringify(res));

          this.successMessage = `Welcome, ${res.roleName} ${res.username}!`;

          const role = res.roleName.toLowerCase();

          if (role === 'customer') {
  const headers = new HttpHeaders().set('Authorization', `Bearer ${res.token}`);
  this.http.get<any>(`http://localhost:8080/api/v1/customers/getCustomerByUserId/${res.userId}`, { headers })
    .subscribe({
      next: () => {
        this.router.navigateByUrl('/customer-dashboard');
      },
      error: (err) => {
        if (err.status === 404) {
          this.router.navigateByUrl('/customer-register');
        } else {
          this.errorMessage = 'Something went wrong while verifying profile.';
          console.error(err);
        }
      }
    });
}
 else {
            // Handle other roles
            const route = this.getDashboardRoute(role);
            if (!route) {
              this.errorMessage = 'Unrecognized role. Please contact support.';
              return;
            }
            this.router.navigateByUrl(route);
          }
        },
        error: (err) => {
          console.error('Login error:', err);
          this.errorMessage = 'Invalid username or password.';
        }
      });
  }

  private getDashboardRoute(roleName: string): string | null {
    switch (roleName.toLowerCase()) {
      case 'customer': return '/customer-dashboard';
      case 'bank employee': return '/employee-dashboard';
      case 'administrator':
      case 'admin': return '/admin-dashboard';
      default: return null;
    }
  }
}
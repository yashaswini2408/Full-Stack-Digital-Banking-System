import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const token = localStorage.getItem('token');

  if (token) {
    return true;
  }

  // Redirect to login if not authenticated
  const router = inject(Router);
  router.navigate(['/login']);
  return false;
};

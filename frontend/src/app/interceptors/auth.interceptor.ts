import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const currentUser = authService.currentUserValue;
  
  if (currentUser?.access_token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${currentUser.access_token}`
      }
    });
  }

  return next(req);
}
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="min-h-screen bg-gray-100 flex items-center justify-center">
      <div class="max-w-md w-full bg-white rounded-lg shadow-md p-8">
        <h2 class="text-2xl font-bold mb-6 text-center">Sign Up</h2>
        
        <div *ngIf="errorMessage" class="mb-4 p-4 text-sm text-red-700 bg-red-100 rounded-lg">
          {{ errorMessage }}
        </div>

        <form [formGroup]="signupForm" (ngSubmit)="onSubmit()" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">First Name</label>
            <input
              type="text"
              formControlName="first_name"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              placeholder="Enter your first name"
            />
            <div *ngIf="signupForm.get('first_name')?.touched && signupForm.get('first_name')?.invalid" class="mt-1 text-sm text-red-600">
              First name is required
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Last Name</label>
            <input
              type="text"
              formControlName="last_name"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              placeholder="Enter your last name"
            />
            <div *ngIf="signupForm.get('last_name')?.touched && signupForm.get('last_name')?.invalid" class="mt-1 text-sm text-red-600">
              Last name is required
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <input
              type="email"
              formControlName="email_address"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              placeholder="Enter your email"
            />
            <div *ngIf="signupForm.get('email_address')?.touched && signupForm.get('email_address')?.invalid" class="mt-1 text-sm text-red-600">
              <span *ngIf="signupForm.get('email_address')?.errors?.['required']">Email is required</span>
              <span *ngIf="signupForm.get('email_address')?.errors?.['email']">Please enter a valid email</span>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700">Password</label>
            <input
              type="password"
              formControlName="password"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              placeholder="Enter your password"
            />
            <div *ngIf="signupForm.get('password')?.touched && signupForm.get('password')?.invalid" class="mt-1 text-sm text-red-600">
              <span *ngIf="signupForm.get('password')?.errors?.['required']">Password is required</span>
              <span *ngIf="signupForm.get('password')?.errors?.['minlength']">Password must be at least 6 characters</span>
            </div>
          </div>

          <button
            type="submit"
            [disabled]="signupForm.invalid || isLoading"
            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
          >
            {{ isLoading ? 'Signing up...' : 'Sign Up' }}
          </button>

          <p class="text-center text-sm text-gray-600">
            Already have an account?
            <a routerLink="/" class="text-indigo-600 hover:text-indigo-500">Login</a>
          </p>
        </form>
      </div>
    </div>
  `
})
export class SignupComponent {
  signupForm: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      first_name: ['', Validators.required],
      last_name: ['', Validators.required],
      email_address: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.signupForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';
      
      this.authService.signup(this.signupForm.value).subscribe({
        next: () => {
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.errorMessage = error.message || 'An error occurred during signup. Please try again.';
          this.isLoading = false;
        }
      });
    }
  }
}
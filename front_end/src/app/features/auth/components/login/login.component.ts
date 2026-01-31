import { Component, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    standalone: false
})
export class LoginComponent {
    loginForm: FormGroup;
    isLoading = false;
    backendErrors: { [key: string]: string } = {};
    generalError: string = '';

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private toastr: ToastrService,
        private cdr: ChangeDetectorRef
    ) {
        this.loginForm = this.fb.group({
            username: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onSubmit() {
        if (this.loginForm.invalid) return;

        this.isLoading = true;
        this.backendErrors = {};
        this.generalError = '';

        this.authService.login(this.loginForm.value).subscribe({
            next: (user) => {
                this.isLoading = false;
                this.cdr.detectChanges();
                this.toastr.success('Logged in successfully');

                const normalizedRole = String(user.role || '').replace('ROLE_', '').trim().toUpperCase();
                if (normalizedRole === 'ADMIN') {
                    this.router.navigate(['/admin']);
                } else {
                    this.router.navigate(['/user']);
                }
            },
            error: (err) => {
                this.isLoading = false;
                if (err.status === 0) {
                    this.generalError = 'Connection failed. Please check your backend and proxy settings.';
                } else if (err.status === 400 && err.error.validationErrors) {
                    this.backendErrors = err.error.validationErrors;
                } else {
                    this.generalError = err.error?.message || 'Invalid credentials';
                }
                this.toastr.error(this.generalError);
                this.cdr.detectChanges();
            }
        });
    }
}

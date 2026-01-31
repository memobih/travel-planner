import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
    standalone: false
})
export class RegisterComponent {
    registerForm: FormGroup;
    isLoading = false;
    backendErrors: { [key: string]: string } = {};
    generalError: string = '';

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private toastr: ToastrService
    ) {
        this.registerForm = this.fb.group({
            username: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onSubmit() {
        if (this.registerForm.invalid) return;

        this.isLoading = true;
        this.backendErrors = {};
        this.generalError = '';

        this.authService.register(this.registerForm.value).subscribe({
            next: (user) => {
                this.isLoading = false;
                this.toastr.success('Registered successfully');
                if (user.role === 'ADMIN') {
                    this.router.navigate(['/admin']);
                } else {
                    this.router.navigate(['/user']);
                }
            },
            error: (err) => {
                this.isLoading = false;
                if (err.status === 400 && err.error.validationErrors) {
                    this.backendErrors = err.error.validationErrors;
                } else {
                    this.generalError = err.error?.message || 'Registration failed';
                    this.toastr.error(this.generalError);
                }
            }
        });
    }
}

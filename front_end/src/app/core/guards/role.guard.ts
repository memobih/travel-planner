import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class RoleGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        const user = this.authService.currentUserValue;
        const expectedRole = route.data['role'];

        if (!user) {
            this.router.navigate(['/auth/login']);
            return false;
        }

        const normalize = (r: any) => String(r || '').replace('ROLE_', '').trim().toUpperCase();
        if (normalize(user.role) === normalize(expectedRole)) {
            return true;
        }

        this.router.navigate(['/auth/login']);
        return false;
    }
}

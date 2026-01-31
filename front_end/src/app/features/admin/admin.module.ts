import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { SharedModule } from '../../shared/shared.module';
import { RoleGuard } from '../../core/guards/role.guard';

const routes: Routes = [
    {
        path: '',
        component: AdminDashboardComponent,
        canActivate: [RoleGuard],
        data: { role: 'ADMIN' }
    }
];

@NgModule({
    declarations: [
        AdminDashboardComponent
    ],
    imports: [
        SharedModule,
        RouterModule.forChild(routes)
    ]
})
export class AdminModule { }

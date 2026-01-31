import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserDashboardComponent } from './components/user-dashboard/user-dashboard.component';
import { SharedModule } from '../../shared/shared.module';
import { RoleGuard } from '../../core/guards/role.guard';

const routes: Routes = [
    {
        path: '',
        component: UserDashboardComponent,
        canActivate: [RoleGuard],
        data: { role: 'USER' }
    }
];

@NgModule({
    declarations: [
        UserDashboardComponent
    ],
    imports: [
        SharedModule,
        RouterModule.forChild(routes)
    ]
})
export class UserModule { }

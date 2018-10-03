import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ConfigCommission } from 'app/shared/model/config-commission.model';
import { ConfigCommissionService } from './config-commission.service';
import { ConfigCommissionComponent } from './config-commission.component';
import { ConfigCommissionDetailComponent } from './config-commission-detail.component';
import { ConfigCommissionUpdateComponent } from './config-commission-update.component';
import { ConfigCommissionDeletePopupComponent } from './config-commission-delete-dialog.component';
import { IConfigCommission } from 'app/shared/model/config-commission.model';

@Injectable({ providedIn: 'root' })
export class ConfigCommissionResolve implements Resolve<IConfigCommission> {
    constructor(private service: ConfigCommissionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((configCommission: HttpResponse<ConfigCommission>) => configCommission.body));
        }
        return of(new ConfigCommission());
    }
}

export const configCommissionRoute: Routes = [
    {
        path: 'config-commission',
        component: ConfigCommissionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configCommission.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'config-commission/:id/view',
        component: ConfigCommissionDetailComponent,
        resolve: {
            configCommission: ConfigCommissionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configCommission.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'config-commission/new',
        component: ConfigCommissionUpdateComponent,
        resolve: {
            configCommission: ConfigCommissionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configCommission.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'config-commission/:id/edit',
        component: ConfigCommissionUpdateComponent,
        resolve: {
            configCommission: ConfigCommissionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configCommission.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const configCommissionPopupRoute: Routes = [
    {
        path: 'config-commission/:id/delete',
        component: ConfigCommissionDeletePopupComponent,
        resolve: {
            configCommission: ConfigCommissionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configCommission.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ConfigFare } from 'app/shared/model/config-fare.model';
import { ConfigFareService } from './config-fare.service';
import { ConfigFareComponent } from './config-fare.component';
import { ConfigFareDetailComponent } from './config-fare-detail.component';
import { ConfigFareUpdateComponent } from './config-fare-update.component';
import { ConfigFareDeletePopupComponent } from './config-fare-delete-dialog.component';
import { IConfigFare } from 'app/shared/model/config-fare.model';

@Injectable({ providedIn: 'root' })
export class ConfigFareResolve implements Resolve<IConfigFare> {
    constructor(private service: ConfigFareService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((configFare: HttpResponse<ConfigFare>) => configFare.body));
        }
        return of(new ConfigFare());
    }
}

export const configFareRoute: Routes = [
    {
        path: 'config-fare',
        component: ConfigFareComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configFare.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'config-fare/:id/view',
        component: ConfigFareDetailComponent,
        resolve: {
            configFare: ConfigFareResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configFare.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'config-fare/new',
        component: ConfigFareUpdateComponent,
        resolve: {
            configFare: ConfigFareResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configFare.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'config-fare/:id/edit',
        component: ConfigFareUpdateComponent,
        resolve: {
            configFare: ConfigFareResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configFare.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const configFarePopupRoute: Routes = [
    {
        path: 'config-fare/:id/delete',
        component: ConfigFareDeletePopupComponent,
        resolve: {
            configFare: ConfigFareResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.configFare.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

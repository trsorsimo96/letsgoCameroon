import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Distributor } from 'app/shared/model/distributor.model';
import { DistributorService } from './distributor.service';
import { DistributorComponent } from './distributor.component';
import { DistributorDetailComponent } from './distributor-detail.component';
import { DistributorUpdateComponent } from './distributor-update.component';
import { DistributorDeletePopupComponent } from './distributor-delete-dialog.component';
import { IDistributor } from 'app/shared/model/distributor.model';

@Injectable({ providedIn: 'root' })
export class DistributorResolve implements Resolve<IDistributor> {
    constructor(private service: DistributorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((distributor: HttpResponse<Distributor>) => distributor.body));
        }
        return of(new Distributor());
    }
}

export const distributorRoute: Routes = [
    {
        path: 'distributor',
        component: DistributorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.distributor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'distributor/:id/view',
        component: DistributorDetailComponent,
        resolve: {
            distributor: DistributorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.distributor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'distributor/new',
        component: DistributorUpdateComponent,
        resolve: {
            distributor: DistributorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.distributor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'distributor/:id/edit',
        component: DistributorUpdateComponent,
        resolve: {
            distributor: DistributorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.distributor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const distributorPopupRoute: Routes = [
    {
        path: 'distributor/:id/delete',
        component: DistributorDeletePopupComponent,
        resolve: {
            distributor: DistributorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.distributor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Cabin } from 'app/shared/model/cabin.model';
import { CabinService } from './cabin.service';
import { CabinComponent } from './cabin.component';
import { CabinDetailComponent } from './cabin-detail.component';
import { CabinUpdateComponent } from './cabin-update.component';
import { CabinDeletePopupComponent } from './cabin-delete-dialog.component';
import { ICabin } from 'app/shared/model/cabin.model';

@Injectable({ providedIn: 'root' })
export class CabinResolve implements Resolve<ICabin> {
    constructor(private service: CabinService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((cabin: HttpResponse<Cabin>) => cabin.body));
        }
        return of(new Cabin());
    }
}

export const cabinRoute: Routes = [
    {
        path: 'cabin',
        component: CabinComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.cabin.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cabin/:id/view',
        component: CabinDetailComponent,
        resolve: {
            cabin: CabinResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.cabin.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cabin/new',
        component: CabinUpdateComponent,
        resolve: {
            cabin: CabinResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.cabin.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cabin/:id/edit',
        component: CabinUpdateComponent,
        resolve: {
            cabin: CabinResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.cabin.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cabinPopupRoute: Routes = [
    {
        path: 'cabin/:id/delete',
        component: CabinDeletePopupComponent,
        resolve: {
            cabin: CabinResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.cabin.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Travel } from 'app/shared/model/travel.model';
import { TravelService } from './travel.service';
import { TravelComponent } from './travel.component';
import { TravelDetailComponent } from './travel-detail.component';
import { TravelUpdateComponent } from './travel-update.component';
import { TravelDeletePopupComponent } from './travel-delete-dialog.component';
import { ITravel } from 'app/shared/model/travel.model';

@Injectable({ providedIn: 'root' })
export class TravelResolve implements Resolve<ITravel> {
    constructor(private service: TravelService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((travel: HttpResponse<Travel>) => travel.body));
        }
        return of(new Travel());
    }
}

export const travelRoute: Routes = [
    {
        path: 'travel',
        component: TravelComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.travel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'travel/:id/view',
        component: TravelDetailComponent,
        resolve: {
            travel: TravelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.travel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'travel/new',
        component: TravelUpdateComponent,
        resolve: {
            travel: TravelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.travel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'travel/:id/edit',
        component: TravelUpdateComponent,
        resolve: {
            travel: TravelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.travel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const travelPopupRoute: Routes = [
    {
        path: 'travel/:id/delete',
        component: TravelDeletePopupComponent,
        resolve: {
            travel: TravelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.travel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Town } from 'app/shared/model/town.model';
import { TownService } from './town.service';
import { TownComponent } from './town.component';
import { TownDetailComponent } from './town-detail.component';
import { TownUpdateComponent } from './town-update.component';
import { TownDeletePopupComponent } from './town-delete-dialog.component';
import { ITown } from 'app/shared/model/town.model';

@Injectable({ providedIn: 'root' })
export class TownResolve implements Resolve<ITown> {
    constructor(private service: TownService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((town: HttpResponse<Town>) => town.body));
        }
        return of(new Town());
    }
}

export const townRoute: Routes = [
    {
        path: 'town',
        component: TownComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.town.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'town/:id/view',
        component: TownDetailComponent,
        resolve: {
            town: TownResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.town.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'town/new',
        component: TownUpdateComponent,
        resolve: {
            town: TownResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.town.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'town/:id/edit',
        component: TownUpdateComponent,
        resolve: {
            town: TownResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.town.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const townPopupRoute: Routes = [
    {
        path: 'town/:id/delete',
        component: TownDeletePopupComponent,
        resolve: {
            town: TownResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.town.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

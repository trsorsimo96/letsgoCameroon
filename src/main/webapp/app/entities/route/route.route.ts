import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Route } from 'app/shared/model/route.model';
import { RouteService } from './route.service';
import { RouteComponent } from './route.component';
import { RouteDetailComponent } from './route-detail.component';
import { RouteUpdateComponent } from './route-update.component';
import { RouteDeletePopupComponent } from './route-delete-dialog.component';
import { IRoute } from 'app/shared/model/route.model';

@Injectable({ providedIn: 'root' })
export class RouteResolve implements Resolve<IRoute> {
    constructor(private service: RouteService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((route: HttpResponse<Route>) => route.body));
        }
        return of(new Route());
    }
}

export const routeRoute: Routes = [
    {
        path: 'route',
        component: RouteComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.route.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'route/:id/view',
        component: RouteDetailComponent,
        resolve: {
            route: RouteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.route.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'route/new',
        component: RouteUpdateComponent,
        resolve: {
            route: RouteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.route.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'route/:id/edit',
        component: RouteUpdateComponent,
        resolve: {
            route: RouteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.route.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const routePopupRoute: Routes = [
    {
        path: 'route/:id/delete',
        component: RouteDeletePopupComponent,
        resolve: {
            route: RouteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.route.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

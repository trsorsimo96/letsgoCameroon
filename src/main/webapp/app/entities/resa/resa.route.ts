import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Resa } from 'app/shared/model/resa.model';
import { ResaService } from './resa.service';
import { ResaComponent } from './resa.component';
import { ResaDetailComponent } from './resa-detail.component';
import { ResaUpdateComponent } from './resa-update.component';
import { ResaDeletePopupComponent } from './resa-delete-dialog.component';
import { IResa } from 'app/shared/model/resa.model';

@Injectable({ providedIn: 'root' })
export class ResaResolve implements Resolve<IResa> {
    constructor(private service: ResaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((resa: HttpResponse<Resa>) => resa.body));
        }
        return of(new Resa());
    }
}

export const resaRoute: Routes = [
    {
        path: 'resa',
        component: ResaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.resa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'resa/:id/view',
        component: ResaDetailComponent,
        resolve: {
            resa: ResaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.resa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'resa/new',
        component: ResaUpdateComponent,
        resolve: {
            resa: ResaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.resa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'resa/:id/edit',
        component: ResaUpdateComponent,
        resolve: {
            resa: ResaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.resa.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const resaPopupRoute: Routes = [
    {
        path: 'resa/:id/delete',
        component: ResaDeletePopupComponent,
        resolve: {
            resa: ResaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.resa.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

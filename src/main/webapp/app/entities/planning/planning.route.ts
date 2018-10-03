import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Planning } from 'app/shared/model/planning.model';
import { PlanningService } from './planning.service';
import { PlanningComponent } from './planning.component';
import { PlanningDetailComponent } from './planning-detail.component';
import { PlanningUpdateComponent } from './planning-update.component';
import { PlanningDeletePopupComponent } from './planning-delete-dialog.component';
import { IPlanning } from 'app/shared/model/planning.model';

@Injectable({ providedIn: 'root' })
export class PlanningResolve implements Resolve<IPlanning> {
    constructor(private service: PlanningService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((planning: HttpResponse<Planning>) => planning.body));
        }
        return of(new Planning());
    }
}

export const planningRoute: Routes = [
    {
        path: 'planning',
        component: PlanningComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.planning.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'planning/:id/view',
        component: PlanningDetailComponent,
        resolve: {
            planning: PlanningResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.planning.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'planning/new',
        component: PlanningUpdateComponent,
        resolve: {
            planning: PlanningResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.planning.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'planning/:id/edit',
        component: PlanningUpdateComponent,
        resolve: {
            planning: PlanningResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.planning.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const planningPopupRoute: Routes = [
    {
        path: 'planning/:id/delete',
        component: PlanningDeletePopupComponent,
        resolve: {
            planning: PlanningResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.planning.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

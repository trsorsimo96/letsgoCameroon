import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Partner } from 'app/shared/model/partner.model';
import { PartnerService } from './partner.service';
import { PartnerComponent } from './partner.component';
import { PartnerDetailComponent } from './partner-detail.component';
import { PartnerUpdateComponent } from './partner-update.component';
import { PartnerDeletePopupComponent } from './partner-delete-dialog.component';
import { IPartner } from 'app/shared/model/partner.model';

@Injectable({ providedIn: 'root' })
export class PartnerResolve implements Resolve<IPartner> {
    constructor(private service: PartnerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((partner: HttpResponse<Partner>) => partner.body));
        }
        return of(new Partner());
    }
}

export const partnerRoute: Routes = [
    {
        path: 'partner',
        component: PartnerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.partner.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'partner/:id/view',
        component: PartnerDetailComponent,
        resolve: {
            partner: PartnerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.partner.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'partner/new',
        component: PartnerUpdateComponent,
        resolve: {
            partner: PartnerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.partner.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'partner/:id/edit',
        component: PartnerUpdateComponent,
        resolve: {
            partner: PartnerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.partner.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const partnerPopupRoute: Routes = [
    {
        path: 'partner/:id/delete',
        component: PartnerDeletePopupComponent,
        resolve: {
            partner: PartnerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'letsgoApp.partner.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

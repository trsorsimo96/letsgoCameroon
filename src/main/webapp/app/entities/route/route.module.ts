import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LetsgoSharedModule } from 'app/shared';
import {
    RouteComponent,
    RouteDetailComponent,
    RouteUpdateComponent,
    RouteDeletePopupComponent,
    RouteDeleteDialogComponent,
    routeRoute,
    routePopupRoute
} from './';

const ENTITY_STATES = [...routeRoute, ...routePopupRoute];

@NgModule({
    imports: [LetsgoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [RouteComponent, RouteDetailComponent, RouteUpdateComponent, RouteDeleteDialogComponent, RouteDeletePopupComponent],
    entryComponents: [RouteComponent, RouteUpdateComponent, RouteDeleteDialogComponent, RouteDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LetsgoRouteModule {}

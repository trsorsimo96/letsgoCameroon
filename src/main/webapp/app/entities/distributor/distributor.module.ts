import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LetsgoSharedModule } from 'app/shared';
import {
    DistributorComponent,
    DistributorDetailComponent,
    DistributorUpdateComponent,
    DistributorDeletePopupComponent,
    DistributorDeleteDialogComponent,
    distributorRoute,
    distributorPopupRoute
} from './';

const ENTITY_STATES = [...distributorRoute, ...distributorPopupRoute];

@NgModule({
    imports: [LetsgoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DistributorComponent,
        DistributorDetailComponent,
        DistributorUpdateComponent,
        DistributorDeleteDialogComponent,
        DistributorDeletePopupComponent
    ],
    entryComponents: [DistributorComponent, DistributorUpdateComponent, DistributorDeleteDialogComponent, DistributorDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LetsgoDistributorModule {}

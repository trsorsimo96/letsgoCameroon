import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LetsgoSharedModule } from 'app/shared';
import {
    PartnerComponent,
    PartnerDetailComponent,
    PartnerUpdateComponent,
    PartnerDeletePopupComponent,
    PartnerDeleteDialogComponent,
    partnerRoute,
    partnerPopupRoute
} from './';

const ENTITY_STATES = [...partnerRoute, ...partnerPopupRoute];

@NgModule({
    imports: [LetsgoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PartnerComponent,
        PartnerDetailComponent,
        PartnerUpdateComponent,
        PartnerDeleteDialogComponent,
        PartnerDeletePopupComponent
    ],
    entryComponents: [PartnerComponent, PartnerUpdateComponent, PartnerDeleteDialogComponent, PartnerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LetsgoPartnerModule {}

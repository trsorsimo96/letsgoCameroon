import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LetsgoSharedModule } from 'app/shared';
import {
    PlanningComponent,
    PlanningDetailComponent,
    PlanningUpdateComponent,
    PlanningDeletePopupComponent,
    PlanningDeleteDialogComponent,
    planningRoute,
    planningPopupRoute
} from './';

const ENTITY_STATES = [...planningRoute, ...planningPopupRoute];

@NgModule({
    imports: [LetsgoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PlanningComponent,
        PlanningDetailComponent,
        PlanningUpdateComponent,
        PlanningDeleteDialogComponent,
        PlanningDeletePopupComponent
    ],
    entryComponents: [PlanningComponent, PlanningUpdateComponent, PlanningDeleteDialogComponent, PlanningDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LetsgoPlanningModule {}

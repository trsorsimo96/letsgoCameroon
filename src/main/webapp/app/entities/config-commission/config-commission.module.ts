import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LetsgoSharedModule } from 'app/shared';
import {
    ConfigCommissionComponent,
    ConfigCommissionDetailComponent,
    ConfigCommissionUpdateComponent,
    ConfigCommissionDeletePopupComponent,
    ConfigCommissionDeleteDialogComponent,
    configCommissionRoute,
    configCommissionPopupRoute
} from './';

const ENTITY_STATES = [...configCommissionRoute, ...configCommissionPopupRoute];

@NgModule({
    imports: [LetsgoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ConfigCommissionComponent,
        ConfigCommissionDetailComponent,
        ConfigCommissionUpdateComponent,
        ConfigCommissionDeleteDialogComponent,
        ConfigCommissionDeletePopupComponent
    ],
    entryComponents: [
        ConfigCommissionComponent,
        ConfigCommissionUpdateComponent,
        ConfigCommissionDeleteDialogComponent,
        ConfigCommissionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LetsgoConfigCommissionModule {}

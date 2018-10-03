import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LetsgoSharedModule } from 'app/shared';
import {
    ResaComponent,
    ResaDetailComponent,
    ResaUpdateComponent,
    ResaDeletePopupComponent,
    ResaDeleteDialogComponent,
    resaRoute,
    resaPopupRoute
} from './';

const ENTITY_STATES = [...resaRoute, ...resaPopupRoute];

@NgModule({
    imports: [LetsgoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ResaComponent, ResaDetailComponent, ResaUpdateComponent, ResaDeleteDialogComponent, ResaDeletePopupComponent],
    entryComponents: [ResaComponent, ResaUpdateComponent, ResaDeleteDialogComponent, ResaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LetsgoResaModule {}

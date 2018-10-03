import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LetsgoPartnerModule } from './partner/partner.module';
import { LetsgoDistributorModule } from './distributor/distributor.module';
import { LetsgoTownModule } from './town/town.module';
import { LetsgoCompanyModule } from './company/company.module';
import { LetsgoRouteModule } from './route/route.module';
import { LetsgoResaModule } from './resa/resa.module';
import { LetsgoTravelModule } from './travel/travel.module';
import { LetsgoConfigFareModule } from './config-fare/config-fare.module';
import { LetsgoConfigCommissionModule } from './config-commission/config-commission.module';
import { LetsgoPlanningModule } from './planning/planning.module';
import { LetsgoCabinModule } from './cabin/cabin.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        LetsgoPartnerModule,
        LetsgoDistributorModule,
        LetsgoTownModule,
        LetsgoCompanyModule,
        LetsgoRouteModule,
        LetsgoResaModule,
        LetsgoTravelModule,
        LetsgoConfigFareModule,
        LetsgoConfigCommissionModule,
        LetsgoPlanningModule,
        LetsgoCabinModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LetsgoEntityModule {}

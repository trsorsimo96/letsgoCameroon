import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfigCommission } from 'app/shared/model/config-commission.model';

@Component({
    selector: 'jhi-config-commission-detail',
    templateUrl: './config-commission-detail.component.html'
})
export class ConfigCommissionDetailComponent implements OnInit {
    configCommission: IConfigCommission;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ configCommission }) => {
            this.configCommission = configCommission;
        });
    }

    previousState() {
        window.history.back();
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfigFare } from 'app/shared/model/config-fare.model';

@Component({
    selector: 'jhi-config-fare-detail',
    templateUrl: './config-fare-detail.component.html'
})
export class ConfigFareDetailComponent implements OnInit {
    configFare: IConfigFare;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ configFare }) => {
            this.configFare = configFare;
        });
    }

    previousState() {
        window.history.back();
    }
}

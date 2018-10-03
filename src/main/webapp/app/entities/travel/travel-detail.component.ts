import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITravel } from 'app/shared/model/travel.model';

@Component({
    selector: 'jhi-travel-detail',
    templateUrl: './travel-detail.component.html'
})
export class TravelDetailComponent implements OnInit {
    travel: ITravel;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ travel }) => {
            this.travel = travel;
        });
    }

    previousState() {
        window.history.back();
    }
}

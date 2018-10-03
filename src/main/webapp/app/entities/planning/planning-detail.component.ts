import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanning } from 'app/shared/model/planning.model';

@Component({
    selector: 'jhi-planning-detail',
    templateUrl: './planning-detail.component.html'
})
export class PlanningDetailComponent implements OnInit {
    planning: IPlanning;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planning }) => {
            this.planning = planning;
        });
    }

    previousState() {
        window.history.back();
    }
}

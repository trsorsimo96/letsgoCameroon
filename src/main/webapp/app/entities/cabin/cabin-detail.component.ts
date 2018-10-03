import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICabin } from 'app/shared/model/cabin.model';

@Component({
    selector: 'jhi-cabin-detail',
    templateUrl: './cabin-detail.component.html'
})
export class CabinDetailComponent implements OnInit {
    cabin: ICabin;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cabin }) => {
            this.cabin = cabin;
        });
    }

    previousState() {
        window.history.back();
    }
}

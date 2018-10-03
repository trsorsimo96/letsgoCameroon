import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResa } from 'app/shared/model/resa.model';

@Component({
    selector: 'jhi-resa-detail',
    templateUrl: './resa-detail.component.html'
})
export class ResaDetailComponent implements OnInit {
    resa: IResa;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ resa }) => {
            this.resa = resa;
        });
    }

    previousState() {
        window.history.back();
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IDistributor } from 'app/shared/model/distributor.model';

@Component({
    selector: 'jhi-distributor-detail',
    templateUrl: './distributor-detail.component.html'
})
export class DistributorDetailComponent implements OnInit {
    distributor: IDistributor;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ distributor }) => {
            this.distributor = distributor;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}

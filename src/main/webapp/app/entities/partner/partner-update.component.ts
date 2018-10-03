import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPartner } from 'app/shared/model/partner.model';
import { PartnerService } from './partner.service';
import { IDistributor } from 'app/shared/model/distributor.model';
import { DistributorService } from 'app/entities/distributor';

@Component({
    selector: 'jhi-partner-update',
    templateUrl: './partner-update.component.html'
})
export class PartnerUpdateComponent implements OnInit {
    private _partner: IPartner;
    isSaving: boolean;

    distributors: IDistributor[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private partnerService: PartnerService,
        private distributorService: DistributorService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ partner }) => {
            this.partner = partner;
        });
        this.distributorService.query().subscribe(
            (res: HttpResponse<IDistributor[]>) => {
                this.distributors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.partner.id !== undefined) {
            this.subscribeToSaveResponse(this.partnerService.update(this.partner));
        } else {
            this.subscribeToSaveResponse(this.partnerService.create(this.partner));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPartner>>) {
        result.subscribe((res: HttpResponse<IPartner>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackDistributorById(index: number, item: IDistributor) {
        return item.id;
    }
    get partner() {
        return this._partner;
    }

    set partner(partner: IPartner) {
        this._partner = partner;
    }
}

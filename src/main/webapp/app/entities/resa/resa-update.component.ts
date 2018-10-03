import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IResa } from 'app/shared/model/resa.model';
import { ResaService } from './resa.service';
import { IPartner } from 'app/shared/model/partner.model';
import { PartnerService } from 'app/entities/partner';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';
import { ITravel } from 'app/shared/model/travel.model';
import { TravelService } from 'app/entities/travel';

@Component({
    selector: 'jhi-resa-update',
    templateUrl: './resa-update.component.html'
})
export class ResaUpdateComponent implements OnInit {
    private _resa: IResa;
    isSaving: boolean;

    partners: IPartner[];

    companies: ICompany[];

    travels: ITravel[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private resaService: ResaService,
        private partnerService: PartnerService,
        private companyService: CompanyService,
        private travelService: TravelService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ resa }) => {
            this.resa = resa;
        });
        this.partnerService.query().subscribe(
            (res: HttpResponse<IPartner[]>) => {
                this.partners = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.companyService.query().subscribe(
            (res: HttpResponse<ICompany[]>) => {
                this.companies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.travelService.query().subscribe(
            (res: HttpResponse<ITravel[]>) => {
                this.travels = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.resa.id !== undefined) {
            this.subscribeToSaveResponse(this.resaService.update(this.resa));
        } else {
            this.subscribeToSaveResponse(this.resaService.create(this.resa));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IResa>>) {
        result.subscribe((res: HttpResponse<IResa>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPartnerById(index: number, item: IPartner) {
        return item.id;
    }

    trackCompanyById(index: number, item: ICompany) {
        return item.id;
    }

    trackTravelById(index: number, item: ITravel) {
        return item.id;
    }
    get resa() {
        return this._resa;
    }

    set resa(resa: IResa) {
        this._resa = resa;
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IConfigCommission } from 'app/shared/model/config-commission.model';
import { ConfigCommissionService } from './config-commission.service';
import { IDistributor } from 'app/shared/model/distributor.model';
import { DistributorService } from 'app/entities/distributor';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
    selector: 'jhi-config-commission-update',
    templateUrl: './config-commission-update.component.html'
})
export class ConfigCommissionUpdateComponent implements OnInit {
    private _configCommission: IConfigCommission;
    isSaving: boolean;

    distributors: IDistributor[];

    companies: ICompany[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private configCommissionService: ConfigCommissionService,
        private distributorService: DistributorService,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ configCommission }) => {
            this.configCommission = configCommission;
        });
        this.distributorService.query().subscribe(
            (res: HttpResponse<IDistributor[]>) => {
                this.distributors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.companyService.query().subscribe(
            (res: HttpResponse<ICompany[]>) => {
                this.companies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.configCommission.id !== undefined) {
            this.subscribeToSaveResponse(this.configCommissionService.update(this.configCommission));
        } else {
            this.subscribeToSaveResponse(this.configCommissionService.create(this.configCommission));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IConfigCommission>>) {
        result.subscribe((res: HttpResponse<IConfigCommission>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCompanyById(index: number, item: ICompany) {
        return item.id;
    }
    get configCommission() {
        return this._configCommission;
    }

    set configCommission(configCommission: IConfigCommission) {
        this._configCommission = configCommission;
    }
}

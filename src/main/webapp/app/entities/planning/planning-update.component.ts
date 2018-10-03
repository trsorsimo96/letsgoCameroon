import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPlanning } from 'app/shared/model/planning.model';
import { PlanningService } from './planning.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';
import { IRoute } from 'app/shared/model/route.model';
import { RouteService } from 'app/entities/route';
import { IConfigFare } from 'app/shared/model/config-fare.model';
import { ConfigFareService } from 'app/entities/config-fare';
import { ICabin } from 'app/shared/model/cabin.model';
import { CabinService } from 'app/entities/cabin';

@Component({
    selector: 'jhi-planning-update',
    templateUrl: './planning-update.component.html'
})
export class PlanningUpdateComponent implements OnInit {
    private _planning: IPlanning;
    isSaving: boolean;

    companies: ICompany[];

    routes: IRoute[];

    configfares: IConfigFare[];

    cabins: ICabin[];
    departureHour: string;
    arrivalHour: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private planningService: PlanningService,
        private companyService: CompanyService,
        private routeService: RouteService,
        private configFareService: ConfigFareService,
        private cabinService: CabinService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ planning }) => {
            this.planning = planning;
        });
        this.companyService.query().subscribe(
            (res: HttpResponse<ICompany[]>) => {
                this.companies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.routeService.query().subscribe(
            (res: HttpResponse<IRoute[]>) => {
                this.routes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.configFareService.query().subscribe(
            (res: HttpResponse<IConfigFare[]>) => {
                this.configfares = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.cabinService.query().subscribe(
            (res: HttpResponse<ICabin[]>) => {
                this.cabins = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.planning.departureHour = moment(this.departureHour, DATE_TIME_FORMAT);
        this.planning.arrivalHour = moment(this.arrivalHour, DATE_TIME_FORMAT);
        if (this.planning.id !== undefined) {
            this.subscribeToSaveResponse(this.planningService.update(this.planning));
        } else {
            this.subscribeToSaveResponse(this.planningService.create(this.planning));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPlanning>>) {
        result.subscribe((res: HttpResponse<IPlanning>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCompanyById(index: number, item: ICompany) {
        return item.id;
    }

    trackRouteById(index: number, item: IRoute) {
        return item.id;
    }

    trackConfigFareById(index: number, item: IConfigFare) {
        return item.id;
    }

    trackCabinById(index: number, item: ICabin) {
        return item.id;
    }
    get planning() {
        return this._planning;
    }

    set planning(planning: IPlanning) {
        this._planning = planning;
        this.departureHour = moment(planning.departureHour).format(DATE_TIME_FORMAT);
        this.arrivalHour = moment(planning.arrivalHour).format(DATE_TIME_FORMAT);
    }
}

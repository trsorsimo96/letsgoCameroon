import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ITravel } from 'app/shared/model/travel.model';
import { TravelService } from './travel.service';
import { IPlanning } from 'app/shared/model/planning.model';
import { PlanningService } from 'app/entities/planning';

@Component({
    selector: 'jhi-travel-update',
    templateUrl: './travel-update.component.html'
})
export class TravelUpdateComponent implements OnInit {
    private _travel: ITravel;
    isSaving: boolean;

    plannings: IPlanning[];
    date: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private travelService: TravelService,
        private planningService: PlanningService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ travel }) => {
            this.travel = travel;
        });
        this.planningService.query().subscribe(
            (res: HttpResponse<IPlanning[]>) => {
                this.plannings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.travel.date = moment(this.date, DATE_TIME_FORMAT);
        if (this.travel.id !== undefined) {
            this.subscribeToSaveResponse(this.travelService.update(this.travel));
        } else {
            this.subscribeToSaveResponse(this.travelService.create(this.travel));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITravel>>) {
        result.subscribe((res: HttpResponse<ITravel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPlanningById(index: number, item: IPlanning) {
        return item.id;
    }
    get travel() {
        return this._travel;
    }

    set travel(travel: ITravel) {
        this._travel = travel;
        this.date = moment(travel.date).format(DATE_TIME_FORMAT);
    }
}

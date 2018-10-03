import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITown } from 'app/shared/model/town.model';
import { TownService } from './town.service';
import { IRoute } from 'app/shared/model/route.model';
import { RouteService } from 'app/entities/route';

@Component({
    selector: 'jhi-town-update',
    templateUrl: './town-update.component.html'
})
export class TownUpdateComponent implements OnInit {
    private _town: ITown;
    isSaving: boolean;

    departures: IRoute[];

    arrivals: IRoute[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private townService: TownService,
        private routeService: RouteService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ town }) => {
            this.town = town;
        });
        this.routeService.query({ filter: 'town-is-null' }).subscribe(
            (res: HttpResponse<IRoute[]>) => {
                if (!this.town.departure || !this.town.departure.id) {
                    this.departures = res.body;
                } else {
                    this.routeService.find(this.town.departure.id).subscribe(
                        (subRes: HttpResponse<IRoute>) => {
                            this.departures = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.routeService.query({ filter: 'town-is-null' }).subscribe(
            (res: HttpResponse<IRoute[]>) => {
                if (!this.town.arrival || !this.town.arrival.id) {
                    this.arrivals = res.body;
                } else {
                    this.routeService.find(this.town.arrival.id).subscribe(
                        (subRes: HttpResponse<IRoute>) => {
                            this.arrivals = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.town.id !== undefined) {
            this.subscribeToSaveResponse(this.townService.update(this.town));
        } else {
            this.subscribeToSaveResponse(this.townService.create(this.town));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITown>>) {
        result.subscribe((res: HttpResponse<ITown>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackRouteById(index: number, item: IRoute) {
        return item.id;
    }
    get town() {
        return this._town;
    }

    set town(town: ITown) {
        this._town = town;
    }
}

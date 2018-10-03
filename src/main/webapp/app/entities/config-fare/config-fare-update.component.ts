import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IConfigFare } from 'app/shared/model/config-fare.model';
import { ConfigFareService } from './config-fare.service';

@Component({
    selector: 'jhi-config-fare-update',
    templateUrl: './config-fare-update.component.html'
})
export class ConfigFareUpdateComponent implements OnInit {
    private _configFare: IConfigFare;
    isSaving: boolean;

    constructor(private configFareService: ConfigFareService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ configFare }) => {
            this.configFare = configFare;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.configFare.id !== undefined) {
            this.subscribeToSaveResponse(this.configFareService.update(this.configFare));
        } else {
            this.subscribeToSaveResponse(this.configFareService.create(this.configFare));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IConfigFare>>) {
        result.subscribe((res: HttpResponse<IConfigFare>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get configFare() {
        return this._configFare;
    }

    set configFare(configFare: IConfigFare) {
        this._configFare = configFare;
    }
}

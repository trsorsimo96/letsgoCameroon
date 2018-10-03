import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICabin } from 'app/shared/model/cabin.model';
import { CabinService } from './cabin.service';

@Component({
    selector: 'jhi-cabin-update',
    templateUrl: './cabin-update.component.html'
})
export class CabinUpdateComponent implements OnInit {
    private _cabin: ICabin;
    isSaving: boolean;

    constructor(private cabinService: CabinService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cabin }) => {
            this.cabin = cabin;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cabin.id !== undefined) {
            this.subscribeToSaveResponse(this.cabinService.update(this.cabin));
        } else {
            this.subscribeToSaveResponse(this.cabinService.create(this.cabin));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICabin>>) {
        result.subscribe((res: HttpResponse<ICabin>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get cabin() {
        return this._cabin;
    }

    set cabin(cabin: ICabin) {
        this._cabin = cabin;
    }
}

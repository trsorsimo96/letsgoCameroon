import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { IDistributor } from 'app/shared/model/distributor.model';
import { DistributorService } from './distributor.service';

@Component({
    selector: 'jhi-distributor-update',
    templateUrl: './distributor-update.component.html'
})
export class DistributorUpdateComponent implements OnInit {
    private _distributor: IDistributor;
    isSaving: boolean;

    constructor(
        private dataUtils: JhiDataUtils,
        private distributorService: DistributorService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
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

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.distributor, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.distributor.id !== undefined) {
            this.subscribeToSaveResponse(this.distributorService.update(this.distributor));
        } else {
            this.subscribeToSaveResponse(this.distributorService.create(this.distributor));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDistributor>>) {
        result.subscribe((res: HttpResponse<IDistributor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get distributor() {
        return this._distributor;
    }

    set distributor(distributor: IDistributor) {
        this._distributor = distributor;
    }
}

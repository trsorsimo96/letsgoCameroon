import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IResa } from 'app/shared/model/resa.model';
import { Principal } from 'app/core';
import { ResaService } from './resa.service';

@Component({
    selector: 'jhi-resa',
    templateUrl: './resa.component.html'
})
export class ResaComponent implements OnInit, OnDestroy {
    resas: IResa[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private resaService: ResaService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.resaService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<IResa[]>) => (this.resas = res.body), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.resaService.query().subscribe(
            (res: HttpResponse<IResa[]>) => {
                this.resas = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInResas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IResa) {
        return item.id;
    }

    registerChangeInResas() {
        this.eventSubscriber = this.eventManager.subscribe('resaListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

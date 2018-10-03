import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IConfigFare } from 'app/shared/model/config-fare.model';
import { Principal } from 'app/core';
import { ConfigFareService } from './config-fare.service';

@Component({
    selector: 'jhi-config-fare',
    templateUrl: './config-fare.component.html'
})
export class ConfigFareComponent implements OnInit, OnDestroy {
    configFares: IConfigFare[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private configFareService: ConfigFareService,
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
            this.configFareService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IConfigFare[]>) => (this.configFares = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.configFareService.query().subscribe(
            (res: HttpResponse<IConfigFare[]>) => {
                this.configFares = res.body;
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
        this.registerChangeInConfigFares();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IConfigFare) {
        return item.id;
    }

    registerChangeInConfigFares() {
        this.eventSubscriber = this.eventManager.subscribe('configFareListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ITown } from 'app/shared/model/town.model';
import { Principal } from 'app/core';
import { TownService } from './town.service';

@Component({
    selector: 'jhi-town',
    templateUrl: './town.component.html'
})
export class TownComponent implements OnInit, OnDestroy {
    towns: ITown[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private townService: TownService,
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
            this.townService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<ITown[]>) => (this.towns = res.body), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.townService.query().subscribe(
            (res: HttpResponse<ITown[]>) => {
                this.towns = res.body;
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
        this.registerChangeInTowns();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ITown) {
        return item.id;
    }

    registerChangeInTowns() {
        this.eventSubscriber = this.eventManager.subscribe('townListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

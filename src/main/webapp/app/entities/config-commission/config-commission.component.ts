import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IConfigCommission } from 'app/shared/model/config-commission.model';
import { Principal } from 'app/core';
import { ConfigCommissionService } from './config-commission.service';

@Component({
    selector: 'jhi-config-commission',
    templateUrl: './config-commission.component.html'
})
export class ConfigCommissionComponent implements OnInit, OnDestroy {
    configCommissions: IConfigCommission[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private configCommissionService: ConfigCommissionService,
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
            this.configCommissionService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IConfigCommission[]>) => (this.configCommissions = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.configCommissionService.query().subscribe(
            (res: HttpResponse<IConfigCommission[]>) => {
                this.configCommissions = res.body;
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
        this.registerChangeInConfigCommissions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IConfigCommission) {
        return item.id;
    }

    registerChangeInConfigCommissions() {
        this.eventSubscriber = this.eventManager.subscribe('configCommissionListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

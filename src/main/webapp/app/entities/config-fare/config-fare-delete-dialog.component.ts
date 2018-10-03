import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConfigFare } from 'app/shared/model/config-fare.model';
import { ConfigFareService } from './config-fare.service';

@Component({
    selector: 'jhi-config-fare-delete-dialog',
    templateUrl: './config-fare-delete-dialog.component.html'
})
export class ConfigFareDeleteDialogComponent {
    configFare: IConfigFare;

    constructor(private configFareService: ConfigFareService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.configFareService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'configFareListModification',
                content: 'Deleted an configFare'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-config-fare-delete-popup',
    template: ''
})
export class ConfigFareDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ configFare }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ConfigFareDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.configFare = configFare;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}

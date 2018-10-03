import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConfigCommission } from 'app/shared/model/config-commission.model';
import { ConfigCommissionService } from './config-commission.service';

@Component({
    selector: 'jhi-config-commission-delete-dialog',
    templateUrl: './config-commission-delete-dialog.component.html'
})
export class ConfigCommissionDeleteDialogComponent {
    configCommission: IConfigCommission;

    constructor(
        private configCommissionService: ConfigCommissionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.configCommissionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'configCommissionListModification',
                content: 'Deleted an configCommission'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-config-commission-delete-popup',
    template: ''
})
export class ConfigCommissionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ configCommission }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ConfigCommissionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.configCommission = configCommission;
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

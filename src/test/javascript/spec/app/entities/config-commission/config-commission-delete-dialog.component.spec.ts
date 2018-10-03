/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LetsgoTestModule } from '../../../test.module';
import { ConfigCommissionDeleteDialogComponent } from 'app/entities/config-commission/config-commission-delete-dialog.component';
import { ConfigCommissionService } from 'app/entities/config-commission/config-commission.service';

describe('Component Tests', () => {
    describe('ConfigCommission Management Delete Component', () => {
        let comp: ConfigCommissionDeleteDialogComponent;
        let fixture: ComponentFixture<ConfigCommissionDeleteDialogComponent>;
        let service: ConfigCommissionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ConfigCommissionDeleteDialogComponent]
            })
                .overrideTemplate(ConfigCommissionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ConfigCommissionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigCommissionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});

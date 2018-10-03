/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LetsgoTestModule } from '../../../test.module';
import { DistributorDeleteDialogComponent } from 'app/entities/distributor/distributor-delete-dialog.component';
import { DistributorService } from 'app/entities/distributor/distributor.service';

describe('Component Tests', () => {
    describe('Distributor Management Delete Component', () => {
        let comp: DistributorDeleteDialogComponent;
        let fixture: ComponentFixture<DistributorDeleteDialogComponent>;
        let service: DistributorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [DistributorDeleteDialogComponent]
            })
                .overrideTemplate(DistributorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DistributorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DistributorService);
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

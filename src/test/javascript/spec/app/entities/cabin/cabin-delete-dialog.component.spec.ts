/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LetsgoTestModule } from '../../../test.module';
import { CabinDeleteDialogComponent } from 'app/entities/cabin/cabin-delete-dialog.component';
import { CabinService } from 'app/entities/cabin/cabin.service';

describe('Component Tests', () => {
    describe('Cabin Management Delete Component', () => {
        let comp: CabinDeleteDialogComponent;
        let fixture: ComponentFixture<CabinDeleteDialogComponent>;
        let service: CabinService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [CabinDeleteDialogComponent]
            })
                .overrideTemplate(CabinDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CabinDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CabinService);
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

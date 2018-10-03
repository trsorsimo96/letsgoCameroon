/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LetsgoTestModule } from '../../../test.module';
import { TravelDeleteDialogComponent } from 'app/entities/travel/travel-delete-dialog.component';
import { TravelService } from 'app/entities/travel/travel.service';

describe('Component Tests', () => {
    describe('Travel Management Delete Component', () => {
        let comp: TravelDeleteDialogComponent;
        let fixture: ComponentFixture<TravelDeleteDialogComponent>;
        let service: TravelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [TravelDeleteDialogComponent]
            })
                .overrideTemplate(TravelDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TravelDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TravelService);
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

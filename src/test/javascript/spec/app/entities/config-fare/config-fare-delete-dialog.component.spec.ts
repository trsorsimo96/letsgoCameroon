/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LetsgoTestModule } from '../../../test.module';
import { ConfigFareDeleteDialogComponent } from 'app/entities/config-fare/config-fare-delete-dialog.component';
import { ConfigFareService } from 'app/entities/config-fare/config-fare.service';

describe('Component Tests', () => {
    describe('ConfigFare Management Delete Component', () => {
        let comp: ConfigFareDeleteDialogComponent;
        let fixture: ComponentFixture<ConfigFareDeleteDialogComponent>;
        let service: ConfigFareService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ConfigFareDeleteDialogComponent]
            })
                .overrideTemplate(ConfigFareDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ConfigFareDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigFareService);
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

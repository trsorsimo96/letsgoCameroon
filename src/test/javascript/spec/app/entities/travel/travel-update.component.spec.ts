/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { TravelUpdateComponent } from 'app/entities/travel/travel-update.component';
import { TravelService } from 'app/entities/travel/travel.service';
import { Travel } from 'app/shared/model/travel.model';

describe('Component Tests', () => {
    describe('Travel Management Update Component', () => {
        let comp: TravelUpdateComponent;
        let fixture: ComponentFixture<TravelUpdateComponent>;
        let service: TravelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [TravelUpdateComponent]
            })
                .overrideTemplate(TravelUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TravelUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TravelService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Travel(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.travel = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Travel();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.travel = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});

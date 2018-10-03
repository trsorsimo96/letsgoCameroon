/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { CabinUpdateComponent } from 'app/entities/cabin/cabin-update.component';
import { CabinService } from 'app/entities/cabin/cabin.service';
import { Cabin } from 'app/shared/model/cabin.model';

describe('Component Tests', () => {
    describe('Cabin Management Update Component', () => {
        let comp: CabinUpdateComponent;
        let fixture: ComponentFixture<CabinUpdateComponent>;
        let service: CabinService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [CabinUpdateComponent]
            })
                .overrideTemplate(CabinUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CabinUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CabinService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Cabin(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.cabin = entity;
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
                    const entity = new Cabin();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.cabin = entity;
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

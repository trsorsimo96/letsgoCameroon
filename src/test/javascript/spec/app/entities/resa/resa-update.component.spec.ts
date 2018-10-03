/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { ResaUpdateComponent } from 'app/entities/resa/resa-update.component';
import { ResaService } from 'app/entities/resa/resa.service';
import { Resa } from 'app/shared/model/resa.model';

describe('Component Tests', () => {
    describe('Resa Management Update Component', () => {
        let comp: ResaUpdateComponent;
        let fixture: ComponentFixture<ResaUpdateComponent>;
        let service: ResaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ResaUpdateComponent]
            })
                .overrideTemplate(ResaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ResaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ResaService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Resa(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.resa = entity;
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
                    const entity = new Resa();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.resa = entity;
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

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { DistributorUpdateComponent } from 'app/entities/distributor/distributor-update.component';
import { DistributorService } from 'app/entities/distributor/distributor.service';
import { Distributor } from 'app/shared/model/distributor.model';

describe('Component Tests', () => {
    describe('Distributor Management Update Component', () => {
        let comp: DistributorUpdateComponent;
        let fixture: ComponentFixture<DistributorUpdateComponent>;
        let service: DistributorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [DistributorUpdateComponent]
            })
                .overrideTemplate(DistributorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DistributorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DistributorService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Distributor(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.distributor = entity;
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
                    const entity = new Distributor();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.distributor = entity;
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

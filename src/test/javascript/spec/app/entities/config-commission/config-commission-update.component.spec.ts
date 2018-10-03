/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { ConfigCommissionUpdateComponent } from 'app/entities/config-commission/config-commission-update.component';
import { ConfigCommissionService } from 'app/entities/config-commission/config-commission.service';
import { ConfigCommission } from 'app/shared/model/config-commission.model';

describe('Component Tests', () => {
    describe('ConfigCommission Management Update Component', () => {
        let comp: ConfigCommissionUpdateComponent;
        let fixture: ComponentFixture<ConfigCommissionUpdateComponent>;
        let service: ConfigCommissionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ConfigCommissionUpdateComponent]
            })
                .overrideTemplate(ConfigCommissionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ConfigCommissionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigCommissionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ConfigCommission(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.configCommission = entity;
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
                    const entity = new ConfigCommission();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.configCommission = entity;
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

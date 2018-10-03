/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { ConfigFareUpdateComponent } from 'app/entities/config-fare/config-fare-update.component';
import { ConfigFareService } from 'app/entities/config-fare/config-fare.service';
import { ConfigFare } from 'app/shared/model/config-fare.model';

describe('Component Tests', () => {
    describe('ConfigFare Management Update Component', () => {
        let comp: ConfigFareUpdateComponent;
        let fixture: ComponentFixture<ConfigFareUpdateComponent>;
        let service: ConfigFareService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ConfigFareUpdateComponent]
            })
                .overrideTemplate(ConfigFareUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ConfigFareUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigFareService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ConfigFare(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.configFare = entity;
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
                    const entity = new ConfigFare();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.configFare = entity;
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

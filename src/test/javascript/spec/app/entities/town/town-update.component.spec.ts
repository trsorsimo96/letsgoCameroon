/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { TownUpdateComponent } from 'app/entities/town/town-update.component';
import { TownService } from 'app/entities/town/town.service';
import { Town } from 'app/shared/model/town.model';

describe('Component Tests', () => {
    describe('Town Management Update Component', () => {
        let comp: TownUpdateComponent;
        let fixture: ComponentFixture<TownUpdateComponent>;
        let service: TownService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [TownUpdateComponent]
            })
                .overrideTemplate(TownUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TownUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TownService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Town(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.town = entity;
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
                    const entity = new Town();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.town = entity;
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

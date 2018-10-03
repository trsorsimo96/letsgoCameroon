/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LetsgoTestModule } from '../../../test.module';
import { PlanningComponent } from 'app/entities/planning/planning.component';
import { PlanningService } from 'app/entities/planning/planning.service';
import { Planning } from 'app/shared/model/planning.model';

describe('Component Tests', () => {
    describe('Planning Management Component', () => {
        let comp: PlanningComponent;
        let fixture: ComponentFixture<PlanningComponent>;
        let service: PlanningService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [PlanningComponent],
                providers: []
            })
                .overrideTemplate(PlanningComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PlanningComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Planning(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.plannings[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});

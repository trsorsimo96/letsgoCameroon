/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { PlanningDetailComponent } from 'app/entities/planning/planning-detail.component';
import { Planning } from 'app/shared/model/planning.model';

describe('Component Tests', () => {
    describe('Planning Management Detail Component', () => {
        let comp: PlanningDetailComponent;
        let fixture: ComponentFixture<PlanningDetailComponent>;
        const route = ({ data: of({ planning: new Planning(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [PlanningDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PlanningDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.planning).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

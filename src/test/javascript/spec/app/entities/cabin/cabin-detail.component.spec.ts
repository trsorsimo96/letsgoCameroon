/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { CabinDetailComponent } from 'app/entities/cabin/cabin-detail.component';
import { Cabin } from 'app/shared/model/cabin.model';

describe('Component Tests', () => {
    describe('Cabin Management Detail Component', () => {
        let comp: CabinDetailComponent;
        let fixture: ComponentFixture<CabinDetailComponent>;
        const route = ({ data: of({ cabin: new Cabin(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [CabinDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CabinDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CabinDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.cabin).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

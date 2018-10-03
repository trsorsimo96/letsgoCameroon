/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { ResaDetailComponent } from 'app/entities/resa/resa-detail.component';
import { Resa } from 'app/shared/model/resa.model';

describe('Component Tests', () => {
    describe('Resa Management Detail Component', () => {
        let comp: ResaDetailComponent;
        let fixture: ComponentFixture<ResaDetailComponent>;
        const route = ({ data: of({ resa: new Resa(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ResaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ResaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ResaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.resa).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

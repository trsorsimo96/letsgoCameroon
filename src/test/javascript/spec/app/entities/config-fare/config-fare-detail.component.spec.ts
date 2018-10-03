/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { ConfigFareDetailComponent } from 'app/entities/config-fare/config-fare-detail.component';
import { ConfigFare } from 'app/shared/model/config-fare.model';

describe('Component Tests', () => {
    describe('ConfigFare Management Detail Component', () => {
        let comp: ConfigFareDetailComponent;
        let fixture: ComponentFixture<ConfigFareDetailComponent>;
        const route = ({ data: of({ configFare: new ConfigFare(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ConfigFareDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ConfigFareDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ConfigFareDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.configFare).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

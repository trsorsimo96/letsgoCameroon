/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LetsgoTestModule } from '../../../test.module';
import { ConfigCommissionDetailComponent } from 'app/entities/config-commission/config-commission-detail.component';
import { ConfigCommission } from 'app/shared/model/config-commission.model';

describe('Component Tests', () => {
    describe('ConfigCommission Management Detail Component', () => {
        let comp: ConfigCommissionDetailComponent;
        let fixture: ComponentFixture<ConfigCommissionDetailComponent>;
        const route = ({ data: of({ configCommission: new ConfigCommission(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ConfigCommissionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ConfigCommissionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ConfigCommissionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.configCommission).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

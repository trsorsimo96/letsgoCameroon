/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LetsgoTestModule } from '../../../test.module';
import { ConfigCommissionComponent } from 'app/entities/config-commission/config-commission.component';
import { ConfigCommissionService } from 'app/entities/config-commission/config-commission.service';
import { ConfigCommission } from 'app/shared/model/config-commission.model';

describe('Component Tests', () => {
    describe('ConfigCommission Management Component', () => {
        let comp: ConfigCommissionComponent;
        let fixture: ComponentFixture<ConfigCommissionComponent>;
        let service: ConfigCommissionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ConfigCommissionComponent],
                providers: []
            })
                .overrideTemplate(ConfigCommissionComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ConfigCommissionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigCommissionService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ConfigCommission(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.configCommissions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});

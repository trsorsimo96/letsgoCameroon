/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LetsgoTestModule } from '../../../test.module';
import { ConfigFareComponent } from 'app/entities/config-fare/config-fare.component';
import { ConfigFareService } from 'app/entities/config-fare/config-fare.service';
import { ConfigFare } from 'app/shared/model/config-fare.model';

describe('Component Tests', () => {
    describe('ConfigFare Management Component', () => {
        let comp: ConfigFareComponent;
        let fixture: ComponentFixture<ConfigFareComponent>;
        let service: ConfigFareService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ConfigFareComponent],
                providers: []
            })
                .overrideTemplate(ConfigFareComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ConfigFareComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigFareService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ConfigFare(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.configFares[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});

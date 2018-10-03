/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LetsgoTestModule } from '../../../test.module';
import { TravelComponent } from 'app/entities/travel/travel.component';
import { TravelService } from 'app/entities/travel/travel.service';
import { Travel } from 'app/shared/model/travel.model';

describe('Component Tests', () => {
    describe('Travel Management Component', () => {
        let comp: TravelComponent;
        let fixture: ComponentFixture<TravelComponent>;
        let service: TravelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [TravelComponent],
                providers: []
            })
                .overrideTemplate(TravelComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TravelComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TravelService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Travel(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.travels[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});

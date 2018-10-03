/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LetsgoTestModule } from '../../../test.module';
import { DistributorComponent } from 'app/entities/distributor/distributor.component';
import { DistributorService } from 'app/entities/distributor/distributor.service';
import { Distributor } from 'app/shared/model/distributor.model';

describe('Component Tests', () => {
    describe('Distributor Management Component', () => {
        let comp: DistributorComponent;
        let fixture: ComponentFixture<DistributorComponent>;
        let service: DistributorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [DistributorComponent],
                providers: []
            })
                .overrideTemplate(DistributorComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DistributorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DistributorService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Distributor(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.distributors[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});

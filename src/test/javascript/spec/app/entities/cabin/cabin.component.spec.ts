/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LetsgoTestModule } from '../../../test.module';
import { CabinComponent } from 'app/entities/cabin/cabin.component';
import { CabinService } from 'app/entities/cabin/cabin.service';
import { Cabin } from 'app/shared/model/cabin.model';

describe('Component Tests', () => {
    describe('Cabin Management Component', () => {
        let comp: CabinComponent;
        let fixture: ComponentFixture<CabinComponent>;
        let service: CabinService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [CabinComponent],
                providers: []
            })
                .overrideTemplate(CabinComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CabinComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CabinService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Cabin(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.cabins[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});

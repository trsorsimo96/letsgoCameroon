/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LetsgoTestModule } from '../../../test.module';
import { ResaComponent } from 'app/entities/resa/resa.component';
import { ResaService } from 'app/entities/resa/resa.service';
import { Resa } from 'app/shared/model/resa.model';

describe('Component Tests', () => {
    describe('Resa Management Component', () => {
        let comp: ResaComponent;
        let fixture: ComponentFixture<ResaComponent>;
        let service: ResaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [ResaComponent],
                providers: []
            })
                .overrideTemplate(ResaComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ResaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ResaService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Resa(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.resas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});

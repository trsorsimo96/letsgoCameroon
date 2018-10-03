/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LetsgoTestModule } from '../../../test.module';
import { TownComponent } from 'app/entities/town/town.component';
import { TownService } from 'app/entities/town/town.service';
import { Town } from 'app/shared/model/town.model';

describe('Component Tests', () => {
    describe('Town Management Component', () => {
        let comp: TownComponent;
        let fixture: ComponentFixture<TownComponent>;
        let service: TownService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LetsgoTestModule],
                declarations: [TownComponent],
                providers: []
            })
                .overrideTemplate(TownComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TownComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TownService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Town(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.towns[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});

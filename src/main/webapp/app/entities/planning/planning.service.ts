import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPlanning } from 'app/shared/model/planning.model';

type EntityResponseType = HttpResponse<IPlanning>;
type EntityArrayResponseType = HttpResponse<IPlanning[]>;

@Injectable({ providedIn: 'root' })
export class PlanningService {
    private resourceUrl = SERVER_API_URL + 'api/plannings';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/plannings';

    constructor(private http: HttpClient) {}

    create(planning: IPlanning): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(planning);
        return this.http
            .post<IPlanning>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(planning: IPlanning): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(planning);
        return this.http
            .put<IPlanning>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPlanning>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPlanning[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPlanning[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(planning: IPlanning): IPlanning {
        const copy: IPlanning = Object.assign({}, planning, {
            departureHour: planning.departureHour != null && planning.departureHour.isValid() ? planning.departureHour.toJSON() : null,
            arrivalHour: planning.arrivalHour != null && planning.arrivalHour.isValid() ? planning.arrivalHour.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.departureHour = res.body.departureHour != null ? moment(res.body.departureHour) : null;
        res.body.arrivalHour = res.body.arrivalHour != null ? moment(res.body.arrivalHour) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((planning: IPlanning) => {
            planning.departureHour = planning.departureHour != null ? moment(planning.departureHour) : null;
            planning.arrivalHour = planning.arrivalHour != null ? moment(planning.arrivalHour) : null;
        });
        return res;
    }
}

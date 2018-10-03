import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IResa } from 'app/shared/model/resa.model';

type EntityResponseType = HttpResponse<IResa>;
type EntityArrayResponseType = HttpResponse<IResa[]>;

@Injectable({ providedIn: 'root' })
export class ResaService {
    private resourceUrl = SERVER_API_URL + 'api/resas';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/resas';

    constructor(private http: HttpClient) {}

    create(resa: IResa): Observable<EntityResponseType> {
        return this.http.post<IResa>(this.resourceUrl, resa, { observe: 'response' });
    }

    update(resa: IResa): Observable<EntityResponseType> {
        return this.http.put<IResa>(this.resourceUrl, resa, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IResa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IResa[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IResa[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

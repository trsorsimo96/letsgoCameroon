import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITown } from 'app/shared/model/town.model';

type EntityResponseType = HttpResponse<ITown>;
type EntityArrayResponseType = HttpResponse<ITown[]>;

@Injectable({ providedIn: 'root' })
export class TownService {
    private resourceUrl = SERVER_API_URL + 'api/towns';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/towns';

    constructor(private http: HttpClient) {}

    create(town: ITown): Observable<EntityResponseType> {
        return this.http.post<ITown>(this.resourceUrl, town, { observe: 'response' });
    }

    update(town: ITown): Observable<EntityResponseType> {
        return this.http.put<ITown>(this.resourceUrl, town, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITown>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITown[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITown[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

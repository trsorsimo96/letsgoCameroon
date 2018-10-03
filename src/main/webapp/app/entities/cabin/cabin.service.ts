import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICabin } from 'app/shared/model/cabin.model';

type EntityResponseType = HttpResponse<ICabin>;
type EntityArrayResponseType = HttpResponse<ICabin[]>;

@Injectable({ providedIn: 'root' })
export class CabinService {
    private resourceUrl = SERVER_API_URL + 'api/cabins';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cabins';

    constructor(private http: HttpClient) {}

    create(cabin: ICabin): Observable<EntityResponseType> {
        return this.http.post<ICabin>(this.resourceUrl, cabin, { observe: 'response' });
    }

    update(cabin: ICabin): Observable<EntityResponseType> {
        return this.http.put<ICabin>(this.resourceUrl, cabin, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICabin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICabin[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICabin[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

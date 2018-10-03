import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDistributor } from 'app/shared/model/distributor.model';

type EntityResponseType = HttpResponse<IDistributor>;
type EntityArrayResponseType = HttpResponse<IDistributor[]>;

@Injectable({ providedIn: 'root' })
export class DistributorService {
    private resourceUrl = SERVER_API_URL + 'api/distributors';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/distributors';

    constructor(private http: HttpClient) {}

    create(distributor: IDistributor): Observable<EntityResponseType> {
        return this.http.post<IDistributor>(this.resourceUrl, distributor, { observe: 'response' });
    }

    update(distributor: IDistributor): Observable<EntityResponseType> {
        return this.http.put<IDistributor>(this.resourceUrl, distributor, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDistributor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDistributor[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDistributor[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConfigCommission } from 'app/shared/model/config-commission.model';

type EntityResponseType = HttpResponse<IConfigCommission>;
type EntityArrayResponseType = HttpResponse<IConfigCommission[]>;

@Injectable({ providedIn: 'root' })
export class ConfigCommissionService {
    private resourceUrl = SERVER_API_URL + 'api/config-commissions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/config-commissions';

    constructor(private http: HttpClient) {}

    create(configCommission: IConfigCommission): Observable<EntityResponseType> {
        return this.http.post<IConfigCommission>(this.resourceUrl, configCommission, { observe: 'response' });
    }

    update(configCommission: IConfigCommission): Observable<EntityResponseType> {
        return this.http.put<IConfigCommission>(this.resourceUrl, configCommission, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IConfigCommission>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConfigCommission[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConfigCommission[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

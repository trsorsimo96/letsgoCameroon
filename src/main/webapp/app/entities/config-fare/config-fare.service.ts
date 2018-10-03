import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConfigFare } from 'app/shared/model/config-fare.model';

type EntityResponseType = HttpResponse<IConfigFare>;
type EntityArrayResponseType = HttpResponse<IConfigFare[]>;

@Injectable({ providedIn: 'root' })
export class ConfigFareService {
    private resourceUrl = SERVER_API_URL + 'api/config-fares';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/config-fares';

    constructor(private http: HttpClient) {}

    create(configFare: IConfigFare): Observable<EntityResponseType> {
        return this.http.post<IConfigFare>(this.resourceUrl, configFare, { observe: 'response' });
    }

    update(configFare: IConfigFare): Observable<EntityResponseType> {
        return this.http.put<IConfigFare>(this.resourceUrl, configFare, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IConfigFare>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConfigFare[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConfigFare[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

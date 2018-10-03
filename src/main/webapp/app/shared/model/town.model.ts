import { IRoute } from 'app/shared/model//route.model';

export interface ITown {
    id?: number;
    name?: string;
    title?: string;
    departure?: IRoute;
    arrival?: IRoute;
}

export class Town implements ITown {
    constructor(public id?: number, public name?: string, public title?: string, public departure?: IRoute, public arrival?: IRoute) {}
}

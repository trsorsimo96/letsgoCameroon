import { IPlanning } from 'app/shared/model//planning.model';

export interface IRoute {
    id?: number;
    title?: string;
    paths?: IPlanning[];
}

export class Route implements IRoute {
    constructor(public id?: number, public title?: string, public paths?: IPlanning[]) {}
}

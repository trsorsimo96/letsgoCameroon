import { IPlanning } from 'app/shared/model//planning.model';

export interface ICabin {
    id?: number;
    name?: string;
    title?: string;
    cabins?: IPlanning[];
}

export class Cabin implements ICabin {
    constructor(public id?: number, public name?: string, public title?: string, public cabins?: IPlanning[]) {}
}

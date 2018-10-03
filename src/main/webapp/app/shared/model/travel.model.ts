import { Moment } from 'moment';
import { IResa } from 'app/shared/model//resa.model';
import { IPlanning } from 'app/shared/model//planning.model';

export interface ITravel {
    id?: number;
    number?: number;
    date?: Moment;
    nbPlace?: number;
    leftPlace?: number;
    travels?: IResa[];
    planning?: IPlanning;
}

export class Travel implements ITravel {
    constructor(
        public id?: number,
        public number?: number,
        public date?: Moment,
        public nbPlace?: number,
        public leftPlace?: number,
        public travels?: IResa[],
        public planning?: IPlanning
    ) {}
}

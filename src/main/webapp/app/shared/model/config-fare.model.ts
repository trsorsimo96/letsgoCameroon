import { IPlanning } from 'app/shared/model//planning.model';

export interface IConfigFare {
    id?: number;
    numero?: number;
    fare?: number;
    cancelable?: boolean;
    penaltyCancel?: number;
    noshow?: boolean;
    penaltyNoShow?: number;
    companies?: IPlanning[];
}

export class ConfigFare implements IConfigFare {
    constructor(
        public id?: number,
        public numero?: number,
        public fare?: number,
        public cancelable?: boolean,
        public penaltyCancel?: number,
        public noshow?: boolean,
        public penaltyNoShow?: number,
        public companies?: IPlanning[]
    ) {
        this.cancelable = this.cancelable || false;
        this.noshow = this.noshow || false;
    }
}

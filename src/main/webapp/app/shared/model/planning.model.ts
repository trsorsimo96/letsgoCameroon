import { Moment } from 'moment';
import { ICompany } from 'app/shared/model//company.model';
import { IRoute } from 'app/shared/model//route.model';
import { IConfigFare } from 'app/shared/model//config-fare.model';
import { ITravel } from 'app/shared/model//travel.model';
import { ICabin } from 'app/shared/model//cabin.model';

export interface IPlanning {
    id?: number;
    mon?: boolean;
    tue?: boolean;
    wed?: boolean;
    thu?: boolean;
    fri?: boolean;
    sat?: boolean;
    sun?: boolean;
    departureHour?: Moment;
    arrivalHour?: Moment;
    company?: ICompany;
    route?: IRoute;
    configFare?: IConfigFare;
    travels?: ITravel[];
    cabin?: ICabin;
}

export class Planning implements IPlanning {
    constructor(
        public id?: number,
        public mon?: boolean,
        public tue?: boolean,
        public wed?: boolean,
        public thu?: boolean,
        public fri?: boolean,
        public sat?: boolean,
        public sun?: boolean,
        public departureHour?: Moment,
        public arrivalHour?: Moment,
        public company?: ICompany,
        public route?: IRoute,
        public configFare?: IConfigFare,
        public travels?: ITravel[],
        public cabin?: ICabin
    ) {
        this.mon = this.mon || false;
        this.tue = this.tue || false;
        this.wed = this.wed || false;
        this.thu = this.thu || false;
        this.fri = this.fri || false;
        this.sat = this.sat || false;
        this.sun = this.sun || false;
    }
}

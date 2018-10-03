import { IPartner } from 'app/shared/model//partner.model';
import { ICompany } from 'app/shared/model//company.model';
import { ITravel } from 'app/shared/model//travel.model';

export interface IResa {
    id?: number;
    passengerName?: string;
    passengerCniNumber?: string;
    email?: string;
    number?: string;
    partner?: IPartner;
    company?: ICompany;
    travel?: ITravel;
}

export class Resa implements IResa {
    constructor(
        public id?: number,
        public passengerName?: string,
        public passengerCniNumber?: string,
        public email?: string,
        public number?: string,
        public partner?: IPartner,
        public company?: ICompany,
        public travel?: ITravel
    ) {}
}

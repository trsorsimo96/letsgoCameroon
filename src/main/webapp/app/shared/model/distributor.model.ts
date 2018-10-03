import { IPartner } from 'app/shared/model//partner.model';
import { IConfigCommission } from 'app/shared/model//config-commission.model';

export interface IDistributor {
    id?: number;
    name?: string;
    email?: string;
    number?: string;
    logoContentType?: string;
    logo?: any;
    distributors?: IPartner[];
    distributors?: IConfigCommission[];
}

export class Distributor implements IDistributor {
    constructor(
        public id?: number,
        public name?: string,
        public email?: string,
        public number?: string,
        public logoContentType?: string,
        public logo?: any,
        public distributors?: IPartner[],
        public distributors?: IConfigCommission[]
    ) {}
}

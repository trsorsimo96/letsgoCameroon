import { IDistributor } from 'app/shared/model//distributor.model';
import { ICompany } from 'app/shared/model//company.model';

export interface IConfigCommission {
    id?: number;
    numero?: number;
    commision?: number;
    commisionPartner?: number;
    distributor?: IDistributor;
    company?: ICompany;
}

export class ConfigCommission implements IConfigCommission {
    constructor(
        public id?: number,
        public numero?: number,
        public commision?: number,
        public commisionPartner?: number,
        public distributor?: IDistributor,
        public company?: ICompany
    ) {}
}

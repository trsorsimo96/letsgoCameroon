import { IResa } from 'app/shared/model//resa.model';
import { IConfigCommission } from 'app/shared/model//config-commission.model';
import { IPlanning } from 'app/shared/model//planning.model';

export interface ICompany {
    id?: number;
    name?: string;
    title?: string;
    email?: string;
    number?: string;
    logoContentType?: string;
    logo?: any;
    resellers?: IResa[];
    companies?: IConfigCommission[];
    companies?: IPlanning[];
}

export class Company implements ICompany {
    constructor(
        public id?: number,
        public name?: string,
        public title?: string,
        public email?: string,
        public number?: string,
        public logoContentType?: string,
        public logo?: any,
        public resellers?: IResa[],
        public companies?: IConfigCommission[],
        public companies?: IPlanning[]
    ) {}
}

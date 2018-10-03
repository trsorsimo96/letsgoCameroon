package cm.com.letsgo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ConfigCommission.
 */
@Entity
@Table(name = "config_commission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "configcommission")
public class ConfigCommission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "commision")
    private Integer commision;

    @Column(name = "commision_partner")
    private Integer commisionPartner;

    @ManyToOne
    @JsonIgnoreProperties("distributors")
    private Distributor distributor;

    @ManyToOne
    @JsonIgnoreProperties("companies")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public ConfigCommission numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCommision() {
        return commision;
    }

    public ConfigCommission commision(Integer commision) {
        this.commision = commision;
        return this;
    }

    public void setCommision(Integer commision) {
        this.commision = commision;
    }

    public Integer getCommisionPartner() {
        return commisionPartner;
    }

    public ConfigCommission commisionPartner(Integer commisionPartner) {
        this.commisionPartner = commisionPartner;
        return this;
    }

    public void setCommisionPartner(Integer commisionPartner) {
        this.commisionPartner = commisionPartner;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public ConfigCommission distributor(Distributor distributor) {
        this.distributor = distributor;
        return this;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Company getCompany() {
        return company;
    }

    public ConfigCommission company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigCommission configCommission = (ConfigCommission) o;
        if (configCommission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configCommission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigCommission{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", commision=" + getCommision() +
            ", commisionPartner=" + getCommisionPartner() +
            "}";
    }
}

package cm.com.letsgo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Partner.
 */
@Entity
@Table(name = "partner")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "partner")
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "solde")
    private Integer solde;

    @OneToMany(mappedBy = "partner")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resa> resellers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("distributors")
    private Distributor distributor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Partner name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSolde() {
        return solde;
    }

    public Partner solde(Integer solde) {
        this.solde = solde;
        return this;
    }

    public void setSolde(Integer solde) {
        this.solde = solde;
    }

    public Set<Resa> getResellers() {
        return resellers;
    }

    public Partner resellers(Set<Resa> resas) {
        this.resellers = resas;
        return this;
    }

    public Partner addReseller(Resa resa) {
        this.resellers.add(resa);
        resa.setPartner(this);
        return this;
    }

    public Partner removeReseller(Resa resa) {
        this.resellers.remove(resa);
        resa.setPartner(null);
        return this;
    }

    public void setResellers(Set<Resa> resas) {
        this.resellers = resas;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public Partner distributor(Distributor distributor) {
        this.distributor = distributor;
        return this;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
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
        Partner partner = (Partner) o;
        if (partner.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), partner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Partner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", solde=" + getSolde() +
            "}";
    }
}

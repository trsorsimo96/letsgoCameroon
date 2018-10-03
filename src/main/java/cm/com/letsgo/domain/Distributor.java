package cm.com.letsgo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Distributor.
 */
@Entity
@Table(name = "distributor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "distributor")
public class Distributor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "jhi_number")
    private String number;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @OneToMany(mappedBy = "distributor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Partner> distributors = new HashSet<>();

    @OneToMany(mappedBy = "distributor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ConfigCommission> distributors = new HashSet<>();

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

    public Distributor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Distributor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public Distributor number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public byte[] getLogo() {
        return logo;
    }

    public Distributor logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public Distributor logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Set<Partner> getDistributors() {
        return distributors;
    }

    public Distributor distributors(Set<Partner> partners) {
        this.distributors = partners;
        return this;
    }

    public Distributor addDistributor(Partner partner) {
        this.distributors.add(partner);
        partner.setDistributor(this);
        return this;
    }

    public Distributor removeDistributor(Partner partner) {
        this.distributors.remove(partner);
        partner.setDistributor(null);
        return this;
    }

    public void setDistributors(Set<Partner> partners) {
        this.distributors = partners;
    }

    public Set<ConfigCommission> getDistributors() {
        return distributors;
    }

    public Distributor distributors(Set<ConfigCommission> configCommissions) {
        this.distributors = configCommissions;
        return this;
    }

    public Distributor addDistributor(ConfigCommission configCommission) {
        this.distributors.add(configCommission);
        configCommission.setDistributor(this);
        return this;
    }

    public Distributor removeDistributor(ConfigCommission configCommission) {
        this.distributors.remove(configCommission);
        configCommission.setDistributor(null);
        return this;
    }

    public void setDistributors(Set<ConfigCommission> configCommissions) {
        this.distributors = configCommissions;
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
        Distributor distributor = (Distributor) o;
        if (distributor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), distributor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Distributor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", number='" + getNumber() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}

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
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "email")
    private String email;

    @Column(name = "jhi_number")
    private String number;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resa> resellers = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ConfigCommission> companies = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Planning> companies = new HashSet<>();

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

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public Company title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public Company email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public Company number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public byte[] getLogo() {
        return logo;
    }

    public Company logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public Company logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Set<Resa> getResellers() {
        return resellers;
    }

    public Company resellers(Set<Resa> resas) {
        this.resellers = resas;
        return this;
    }

    public Company addReseller(Resa resa) {
        this.resellers.add(resa);
        resa.setCompany(this);
        return this;
    }

    public Company removeReseller(Resa resa) {
        this.resellers.remove(resa);
        resa.setCompany(null);
        return this;
    }

    public void setResellers(Set<Resa> resas) {
        this.resellers = resas;
    }

    public Set<ConfigCommission> getCompanies() {
        return companies;
    }

    public Company companies(Set<ConfigCommission> configCommissions) {
        this.companies = configCommissions;
        return this;
    }

    public Company addCompany(ConfigCommission configCommission) {
        this.companies.add(configCommission);
        configCommission.setCompany(this);
        return this;
    }

    public Company removeCompany(ConfigCommission configCommission) {
        this.companies.remove(configCommission);
        configCommission.setCompany(null);
        return this;
    }

    public void setCompanies(Set<ConfigCommission> configCommissions) {
        this.companies = configCommissions;
    }

    public Set<Planning> getCompanies() {
        return companies;
    }

    public Company companies(Set<Planning> plannings) {
        this.companies = plannings;
        return this;
    }

    public Company addCompany(Planning planning) {
        this.companies.add(planning);
        planning.setCompany(this);
        return this;
    }

    public Company removeCompany(Planning planning) {
        this.companies.remove(planning);
        planning.setCompany(null);
        return this;
    }

    public void setCompanies(Set<Planning> plannings) {
        this.companies = plannings;
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
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", email='" + getEmail() + "'" +
            ", number='" + getNumber() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}

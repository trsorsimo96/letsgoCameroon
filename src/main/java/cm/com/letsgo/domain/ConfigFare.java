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
 * A ConfigFare.
 */
@Entity
@Table(name = "config_fare")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "configfare")
public class ConfigFare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "fare")
    private Integer fare;

    @Column(name = "cancelable")
    private Boolean cancelable;

    @Column(name = "penalty_cancel")
    private Integer penaltyCancel;

    @Column(name = "noshow")
    private Boolean noshow;

    @Column(name = "penalty_no_show")
    private Integer penaltyNoShow;

    @OneToMany(mappedBy = "configFare")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Planning> companies = new HashSet<>();

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

    public ConfigFare numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getFare() {
        return fare;
    }

    public ConfigFare fare(Integer fare) {
        this.fare = fare;
        return this;
    }

    public void setFare(Integer fare) {
        this.fare = fare;
    }

    public Boolean isCancelable() {
        return cancelable;
    }

    public ConfigFare cancelable(Boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public void setCancelable(Boolean cancelable) {
        this.cancelable = cancelable;
    }

    public Integer getPenaltyCancel() {
        return penaltyCancel;
    }

    public ConfigFare penaltyCancel(Integer penaltyCancel) {
        this.penaltyCancel = penaltyCancel;
        return this;
    }

    public void setPenaltyCancel(Integer penaltyCancel) {
        this.penaltyCancel = penaltyCancel;
    }

    public Boolean isNoshow() {
        return noshow;
    }

    public ConfigFare noshow(Boolean noshow) {
        this.noshow = noshow;
        return this;
    }

    public void setNoshow(Boolean noshow) {
        this.noshow = noshow;
    }

    public Integer getPenaltyNoShow() {
        return penaltyNoShow;
    }

    public ConfigFare penaltyNoShow(Integer penaltyNoShow) {
        this.penaltyNoShow = penaltyNoShow;
        return this;
    }

    public void setPenaltyNoShow(Integer penaltyNoShow) {
        this.penaltyNoShow = penaltyNoShow;
    }

    public Set<Planning> getCompanies() {
        return companies;
    }

    public ConfigFare companies(Set<Planning> plannings) {
        this.companies = plannings;
        return this;
    }

    public ConfigFare addCompany(Planning planning) {
        this.companies.add(planning);
        planning.setConfigFare(this);
        return this;
    }

    public ConfigFare removeCompany(Planning planning) {
        this.companies.remove(planning);
        planning.setConfigFare(null);
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
        ConfigFare configFare = (ConfigFare) o;
        if (configFare.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configFare.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigFare{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", fare=" + getFare() +
            ", cancelable='" + isCancelable() + "'" +
            ", penaltyCancel=" + getPenaltyCancel() +
            ", noshow='" + isNoshow() + "'" +
            ", penaltyNoShow=" + getPenaltyNoShow() +
            "}";
    }
}

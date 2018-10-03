package cm.com.letsgo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Travel.
 */
@Entity
@Table(name = "travel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "travel")
public class Travel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_number")
    private Integer number;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "nb_place")
    private Integer nbPlace;

    @Column(name = "left_place")
    private Integer leftPlace;

    @OneToMany(mappedBy = "travel")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resa> travels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("travels")
    private Planning planning;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public Travel number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Travel date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getNbPlace() {
        return nbPlace;
    }

    public Travel nbPlace(Integer nbPlace) {
        this.nbPlace = nbPlace;
        return this;
    }

    public void setNbPlace(Integer nbPlace) {
        this.nbPlace = nbPlace;
    }

    public Integer getLeftPlace() {
        return leftPlace;
    }

    public Travel leftPlace(Integer leftPlace) {
        this.leftPlace = leftPlace;
        return this;
    }

    public void setLeftPlace(Integer leftPlace) {
        this.leftPlace = leftPlace;
    }

    public Set<Resa> getTravels() {
        return travels;
    }

    public Travel travels(Set<Resa> resas) {
        this.travels = resas;
        return this;
    }

    public Travel addTravel(Resa resa) {
        this.travels.add(resa);
        resa.setTravel(this);
        return this;
    }

    public Travel removeTravel(Resa resa) {
        this.travels.remove(resa);
        resa.setTravel(null);
        return this;
    }

    public void setTravels(Set<Resa> resas) {
        this.travels = resas;
    }

    public Planning getPlanning() {
        return planning;
    }

    public Travel planning(Planning planning) {
        this.planning = planning;
        return this;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
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
        Travel travel = (Travel) o;
        if (travel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), travel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Travel{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", date='" + getDate() + "'" +
            ", nbPlace=" + getNbPlace() +
            ", leftPlace=" + getLeftPlace() +
            "}";
    }
}

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
 * A Planning.
 */
@Entity
@Table(name = "planning")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "planning")
public class Planning implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mon")
    private Boolean mon;

    @Column(name = "tue")
    private Boolean tue;

    @Column(name = "wed")
    private Boolean wed;

    @Column(name = "thu")
    private Boolean thu;

    @Column(name = "fri")
    private Boolean fri;

    @Column(name = "sat")
    private Boolean sat;

    @Column(name = "sun")
    private Boolean sun;

    @Column(name = "departure_hour")
    private ZonedDateTime departureHour;

    @Column(name = "arrival_hour")
    private ZonedDateTime arrivalHour;

    @ManyToOne
    @JsonIgnoreProperties("companies")
    private Company company;

    @ManyToOne
    @JsonIgnoreProperties("paths")
    private Route route;

    @ManyToOne
    @JsonIgnoreProperties("companies")
    private ConfigFare configFare;

    @OneToMany(mappedBy = "planning")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Travel> travels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("cabins")
    private Cabin cabin;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isMon() {
        return mon;
    }

    public Planning mon(Boolean mon) {
        this.mon = mon;
        return this;
    }

    public void setMon(Boolean mon) {
        this.mon = mon;
    }

    public Boolean isTue() {
        return tue;
    }

    public Planning tue(Boolean tue) {
        this.tue = tue;
        return this;
    }

    public void setTue(Boolean tue) {
        this.tue = tue;
    }

    public Boolean isWed() {
        return wed;
    }

    public Planning wed(Boolean wed) {
        this.wed = wed;
        return this;
    }

    public void setWed(Boolean wed) {
        this.wed = wed;
    }

    public Boolean isThu() {
        return thu;
    }

    public Planning thu(Boolean thu) {
        this.thu = thu;
        return this;
    }

    public void setThu(Boolean thu) {
        this.thu = thu;
    }

    public Boolean isFri() {
        return fri;
    }

    public Planning fri(Boolean fri) {
        this.fri = fri;
        return this;
    }

    public void setFri(Boolean fri) {
        this.fri = fri;
    }

    public Boolean isSat() {
        return sat;
    }

    public Planning sat(Boolean sat) {
        this.sat = sat;
        return this;
    }

    public void setSat(Boolean sat) {
        this.sat = sat;
    }

    public Boolean isSun() {
        return sun;
    }

    public Planning sun(Boolean sun) {
        this.sun = sun;
        return this;
    }

    public void setSun(Boolean sun) {
        this.sun = sun;
    }

    public ZonedDateTime getDepartureHour() {
        return departureHour;
    }

    public Planning departureHour(ZonedDateTime departureHour) {
        this.departureHour = departureHour;
        return this;
    }

    public void setDepartureHour(ZonedDateTime departureHour) {
        this.departureHour = departureHour;
    }

    public ZonedDateTime getArrivalHour() {
        return arrivalHour;
    }

    public Planning arrivalHour(ZonedDateTime arrivalHour) {
        this.arrivalHour = arrivalHour;
        return this;
    }

    public void setArrivalHour(ZonedDateTime arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public Company getCompany() {
        return company;
    }

    public Planning company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Route getRoute() {
        return route;
    }

    public Planning route(Route route) {
        this.route = route;
        return this;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public ConfigFare getConfigFare() {
        return configFare;
    }

    public Planning configFare(ConfigFare configFare) {
        this.configFare = configFare;
        return this;
    }

    public void setConfigFare(ConfigFare configFare) {
        this.configFare = configFare;
    }

    public Set<Travel> getTravels() {
        return travels;
    }

    public Planning travels(Set<Travel> travels) {
        this.travels = travels;
        return this;
    }

    public Planning addTravel(Travel travel) {
        this.travels.add(travel);
        travel.setPlanning(this);
        return this;
    }

    public Planning removeTravel(Travel travel) {
        this.travels.remove(travel);
        travel.setPlanning(null);
        return this;
    }

    public void setTravels(Set<Travel> travels) {
        this.travels = travels;
    }

    public Cabin getCabin() {
        return cabin;
    }

    public Planning cabin(Cabin cabin) {
        this.cabin = cabin;
        return this;
    }

    public void setCabin(Cabin cabin) {
        this.cabin = cabin;
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
        Planning planning = (Planning) o;
        if (planning.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planning.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Planning{" +
            "id=" + getId() +
            ", mon='" + isMon() + "'" +
            ", tue='" + isTue() + "'" +
            ", wed='" + isWed() + "'" +
            ", thu='" + isThu() + "'" +
            ", fri='" + isFri() + "'" +
            ", sat='" + isSat() + "'" +
            ", sun='" + isSun() + "'" +
            ", departureHour='" + getDepartureHour() + "'" +
            ", arrivalHour='" + getArrivalHour() + "'" +
            "}";
    }
}

package cm.com.letsgo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Town.
 */
@Entity
@Table(name = "town")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "town")
public class Town implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @OneToOne
    @JoinColumn(unique = true)
    private Route departure;

    @OneToOne
    @JoinColumn(unique = true)
    private Route arrival;

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

    public Town name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public Town title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Route getDeparture() {
        return departure;
    }

    public Town departure(Route route) {
        this.departure = route;
        return this;
    }

    public void setDeparture(Route route) {
        this.departure = route;
    }

    public Route getArrival() {
        return arrival;
    }

    public Town arrival(Route route) {
        this.arrival = route;
        return this;
    }

    public void setArrival(Route route) {
        this.arrival = route;
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
        Town town = (Town) o;
        if (town.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), town.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Town{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}

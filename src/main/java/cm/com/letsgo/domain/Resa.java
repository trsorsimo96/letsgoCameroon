package cm.com.letsgo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Resa.
 */
@Entity
@Table(name = "resa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "resa")
public class Resa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "passenger_cni_number")
    private String passengerCniNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "jhi_number")
    private String number;

    @ManyToOne
    @JsonIgnoreProperties("resellers")
    private Partner partner;

    @ManyToOne
    @JsonIgnoreProperties("resellers")
    private Company company;

    @ManyToOne
    @JsonIgnoreProperties("travels")
    private Travel travel;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Resa passengerName(String passengerName) {
        this.passengerName = passengerName;
        return this;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerCniNumber() {
        return passengerCniNumber;
    }

    public Resa passengerCniNumber(String passengerCniNumber) {
        this.passengerCniNumber = passengerCniNumber;
        return this;
    }

    public void setPassengerCniNumber(String passengerCniNumber) {
        this.passengerCniNumber = passengerCniNumber;
    }

    public String getEmail() {
        return email;
    }

    public Resa email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public Resa number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Partner getPartner() {
        return partner;
    }

    public Resa partner(Partner partner) {
        this.partner = partner;
        return this;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Company getCompany() {
        return company;
    }

    public Resa company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Travel getTravel() {
        return travel;
    }

    public Resa travel(Travel travel) {
        this.travel = travel;
        return this;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
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
        Resa resa = (Resa) o;
        if (resa.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resa.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Resa{" +
            "id=" + getId() +
            ", passengerName='" + getPassengerName() + "'" +
            ", passengerCniNumber='" + getPassengerCniNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", number='" + getNumber() + "'" +
            "}";
    }
}

package com.equiax.employeerecord.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sheet.
 */
@Entity
@Table(name = "sheet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sheet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 20)
    @Column(name = "projectname")
    private String projectname;

    @NotNull
    @Column(name = "weekending", nullable = false)
    private ZonedDateTime weekending;

    @OneToMany(mappedBy = "sheet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LineItem> lineitemss = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public ZonedDateTime getWeekending() {
        return weekending;
    }

    public void setWeekending(ZonedDateTime weekending) {
        this.weekending = weekending;
    }

    public Set<LineItem> getLineitemss() {
        return lineitemss;
    }

    public void setLineitemss(Set<LineItem> lineItems) {
        this.lineitemss = lineItems;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee Employee) {
        this.employee = Employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sheet sheet = (Sheet) o;
        return Objects.equals(id, sheet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sheet{" +
            "id=" + id +
            ", projectname='" + projectname + "'" +
            ", weekending='" + weekending + "'" +
            '}';
    }
}

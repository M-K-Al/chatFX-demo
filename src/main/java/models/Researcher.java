package models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;

@SuppressWarnings("unused")
public class Researcher {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    @Nullable
    private Date onlineSince;
    private Date birthdate;
    private Date joinDate;

    public Researcher(@NotNull final String id,
                      final String firstName,
                      final String lastName,
                      final String email,
                      @Nullable final Date onlineSince,
                      final Date birthdate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.onlineSince = onlineSince;
        this.birthdate = birthdate;
    }

    public String getId() {
        return id;
    }

    public Researcher setId(String id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Researcher setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Researcher setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFullName() {
        return "%s, %s".formatted(lastName, firstName);
    }

    public String getEmail() {
        return email;
    }

    public Researcher setEmail(String email) {
        this.email = email;
        return this;
    }

    public @Nullable Date getOnlineSince() {
        return onlineSince;
    }

    public Researcher setOnlineSince(@Nullable Date onlineSince) {
        this.onlineSince = onlineSince;
        return this;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public Researcher setBirthdate(Date birthDate) {
        this.birthdate = birthDate;
        return this;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public Researcher setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Researcher) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Researcher{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthdate +
                '}';
    }
}

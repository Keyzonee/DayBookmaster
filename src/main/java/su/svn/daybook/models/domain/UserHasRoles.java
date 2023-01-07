/*
 * This file was last modified at 2023.01.05 18:24 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserHasRoles.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.models.LongIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UserHasRoles implements LongIdentification, Serializable {

    public static final String NONE = "4acd4523-e27d-43e7-88dc-f40637c98bf1";
    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = 4722484157347652332L;
    @DomainField
    private final Long id;
    @DomainField(nullable = false)
    private final String userName;
    @DomainField
    private final String role;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public UserHasRoles() {
        this.id = null;
        this.userName = NONE;
        this.role = null;
        this.visible = true;
        this.flags = 0;
    }

    public UserHasRoles(
            Long id,
            @Nonnull String userName,
            String role,
            boolean visible,
            int flags) {
        this.id = id;
        this.userName = userName;
        this.role = role;
        this.visible = visible;
        this.flags = flags;
    }

    public static UserHasRoles.Builder builder() {
        return new UserHasRoles.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    public boolean getVisible() {
        return visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (UserHasRoles) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(userName, that.userName)
                && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && !hashIsZero) {
            h = calculateHashCode();
            if (h == 0) {
                hashIsZero = true;
            } else {
                hash = h;
            }
        }
        return h;
    }

    private int calculateHashCode() {
        return Objects.hash(id, userName, role, visible, flags);
    }

    @Override
    public String toString() {
        return "UserHasRoles{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String userName;
        private String role;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userName(@Nonnull String userName) {
            this.userName = userName;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder flags(int flags) {
            this.flags = flags;
            return this;
        }

        public UserHasRoles build() {
            return new UserHasRoles(id, userName, role, visible, flags);
        }
    }
}

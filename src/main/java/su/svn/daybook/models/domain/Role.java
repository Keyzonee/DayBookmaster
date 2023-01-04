/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Role.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.models.UUIDIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Role implements UUIDIdentification, Serializable {

    public static final String NONE = "adc2221c-03dd-496f-9243-17ed9270b95e";
    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = 5979984741157131705L;
    @DomainField
    private final UUID id;
    @DomainField(nullable = false)
    private final String role;
    @DomainField
    private final String description;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public Role() {
        this.id = null;
        this.role = NONE;
        this.description = null;
        this.visible = true;
        this.flags = 0;
    }

    public Role(
            UUID id,
            @Nonnull String role,
            String description,
            boolean visible,
            int flags) {
        this.id = id;
        this.role = role;
        this.description = description;
        this.visible = visible;
        this.flags = flags;
    }

    public static Role.Builder builder() {
        return new Role.Builder();
    }

    public UUID getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
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
        var that = (Role) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(role, that.role)
                && Objects.equals(description, that.description);
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
        return Objects.hash(id, role, description, visible, flags);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private UUID id;
        private String role;
        private String description;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder role(@Nonnull String role) {
            this.role = role;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
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

        public Role build() {
            return new Role(id, role, description, visible, flags);
        }
    }
}

package com.sample.spring.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Objects;

@Getter
@Setter
@FieldNameConstants
public class IdentityEntity {
    @Id
    @Indexed
    protected String id;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof IdentityEntity
                && this.id != null
                && this.id.equals(((IdentityEntity) that).getId());
    }
}

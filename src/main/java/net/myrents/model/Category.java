package net.myrents.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseEntity {

    @Size(max = 35, message = "אורך השדה יכול להכיל עד {max} תווים")
    private String name;

    private String type;

    public Category() {
    }

}

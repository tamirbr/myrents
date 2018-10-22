package net.myrents.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class BookMark extends BaseEntity {

    @ManyToOne
    private Item item;

    @ManyToOne
    private User user;

    public BookMark() {
        super();
    }

    public BookMark(Item item, User user) {
        super();
        this.item = item;
        this.user = user;
    }

}

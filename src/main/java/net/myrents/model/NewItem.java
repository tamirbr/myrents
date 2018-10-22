package net.myrents.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewItem {
    private Long id;
    private String name;

    public NewItem(Item item) {
        this.id = item.getId();
        this.name = item.getName();
    }

}

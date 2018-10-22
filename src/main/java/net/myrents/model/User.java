package net.myrents.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseEntity {

    @Size(min = 2, max = 15, message = "אורך השדה יכול להכיל בין {min} - {max} תווים")
    private String firstName;

    @Size(min = 2, max = 15, message = "אורך השדה יכול להכיל בין {min} - {max} תווים")
    private String lastName;

    @NotEmpty(message = "יש למלא שדה זה")
    private String phone;

    @NotEmpty(message = "יש למלא שדה זה")
    @Email(message = "דואר אלקטרוני אינו תקין")
    @Column(unique=true)
    private String email;

    @Lob
    private byte[] image;

    private String address;
    @NotNull
    private Double lat;
    private Double lng;

    private String[] roles;

    @Size(min = 2, max = 15, message = "אורך השדה יכול להכיל בין {min} - {max} תווים")
    private String password;

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
    private List<BookMark> bookMarks = new ArrayList<>();

    public User() {
        super();
    }

    public BookMark getBookMarkById(Long id) {
        for(int i = 0; i < this.bookMarks.size();i++){
            if(id.equals(this.bookMarks.get(i).getItem().getId())){
                return this.bookMarks.get(i);
            }
        }
        return null;
    }

}

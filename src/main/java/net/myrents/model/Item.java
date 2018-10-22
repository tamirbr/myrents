package net.myrents.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Item extends BaseEntity implements Comparable<Item> {

    public enum PriceType {
        hour{public String toString() {
            return ConstantsHe.HOUR;
        }},
        day{public String toString() {
            return ConstantsHe.DAY;
        }},
        month{public String toString() {
            return ConstantsHe.MONTH;
        }},

    }

    @ManyToOne
    private User user;

    @NotBlank(message = "יש למלא שדה זה")
    @Size(max = 35, message = "אורך השדה יכול להכיל עד {max} תווים")
    private String name;

    @NotBlank(message = "יש למלא שדה זה")
    @Size(max = 150, message = "אורך השדה יכול להכיל עד {max} תווים")
    private String comment;

    // Using String to allow Pattern
    @Pattern(regexp="^(0|[1-9][0-9]*)?",message = "יש למלא שדה זה")
    @Size(max = 9, message = "אורך השדה יכול להכיל עד {max} מספרים")
    private String priceHour;

    // Using String to allow Pattern
    @Pattern(regexp="^(0|[1-9][0-9]*)?",message = "יש למלא שדה זה")
    @Size(max = 9, message = "אורך השדה יכול להכיל עד {max} מספרים")
    private String priceDay;

    // Using String to allow Pattern
    @Pattern(regexp="^(0|[1-9][0-9]*)?",message = "יש למלא שדה זה")
    @Size(max = 9, message = "אורך השדה יכול להכיל עד {max} מספרים")
    private String priceMonth;

    @NotBlank(message = "יש למלא שדה זה")
    private String address;

    private Double lat;
    private Double lng;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private Date postDate;

    @Lob
    private byte[] image;

    private String groupType;

    private Long category_id;

    private Long subCategory_id;

    private boolean active;

    private Double tempDistance;

//    @OneToMany(mappedBy = "item", cascade=CascadeType.ALL)
//    private List<BookMark> bookMarks = new ArrayList<>();


    public String getPostDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(postDate);
    }

    public String generateBase64Image() {
        return Base64.encodeBase64String(this.getImage());
    }

    @Override
    public int compareTo(Item b) {
        if(tempDistance != null){
            return this.tempDistance < b.tempDistance ? -1
                    : this.tempDistance > b.tempDistance ? 1
                    : 0;
        } else{
            return this.postDate.getTime() > b.postDate.getTime() ? -1
                    : this.postDate.getTime() < b.postDate.getTime() ? 1
                    : 0;
        }
    }

    public boolean isNotEmptyOrNull(String string) {
        switch (string){
            case ConstantsHe.HOUR:
                return !(this.priceHour == null || this.priceHour.isEmpty());
            case ConstantsHe.DAY:
                return !(this.priceDay == null || this.priceDay.isEmpty());
            case ConstantsHe.MONTH:
                return !(this.priceMonth == null || this.priceMonth.isEmpty());
            default:
                return false;
        }
    }
}

package models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "userId", "description", "date", "hours", "minutes"})
public class Task {
    private int id;
    private Integer userId;
    private String title;
    private String description;
    private String date;
    private Integer hours;
    private Integer minutes;
}

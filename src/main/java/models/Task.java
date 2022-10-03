package models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "title", "description", "date", "tracking"})
public class Task {
    private int id;
    private String title;
    private String description;
    private String date;
    private int tracking;
}

package gorest;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    String name;
    String email;
    String gender;
    String status;
}

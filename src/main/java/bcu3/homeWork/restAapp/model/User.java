package bcu3.homeWork.restAapp.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class User extends RepresentationModel<User> {

    private Long id;

    private String firstName;

    private String lastName;

    List<Note> notes;
}

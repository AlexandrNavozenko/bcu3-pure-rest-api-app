package bcu3.homeWork.restAapp.controller;

import bcu3.homeWork.restAapp.model.Note;
import bcu3.homeWork.restAapp.model.User;
import bcu3.homeWork.restAapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private static final String PATH_ID = "/{id}";

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        userService.addUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(PATH_ID)
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body("User add successfully");
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        var user = userService.getById(id);
        if (user.getLinks().isEmpty()) {
            user.add(buildGetUserLink(id))
                    .add(buildRemoveUserLink(id))
                    .add(buildGetNotesByUserLink(id))
                    .add(buildGetAllUserLink());
        }

        return ResponseEntity.ok()
                .body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeUser(@PathVariable Long id) {
        userService.removeUser(id);

        return ResponseEntity.ok().body("User remove successfully");
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<String> addNotes(@PathVariable Long id, @RequestBody Note note) {
        userService.addUserNote(id, note);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();

        return ResponseEntity.created(location).body("Note add to user successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        var users = userService.findAll();
        users.forEach(user -> {
            var id = user.getId();
            if (user.getLinks().isEmpty()) {
                user.add(buildGetUserLink(id))
                        .add(buildRemoveUserLink(id))
                        .add(buildGetNotesByUserLink(id))
                        .add(buildGetAllUserLink());
            }
        });

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}/notes")
    public ResponseEntity<List<?>> getAllNotesByUser(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(userService.findAllByUser(id));
    }

    private Link buildGetUserLink(Long id) {
        return WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(id)).withRel("getById");
    }

    private Link buildRemoveUserLink(Long id) {
        return WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UserController.class).removeUser(id)).withRel("removeById");
    }

    private Link buildGetNotesByUserLink(Long id) {
        return WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UserController.class).getAllNotesByUser(id)).withRel("getAllNotesByUserId");
    }

    private Link buildGetAllUserLink() {
        return WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UserController.class).getAllUsers()).withRel("getAllUsers");
    }
}

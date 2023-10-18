package bcu3.homeWork.restAapp.service;

import bcu3.homeWork.restAapp.model.Note;
import bcu3.homeWork.restAapp.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User getById(Long userId);

    void addUser(User user);

    void removeUser(Long id);

    void addUserNote(Long userId, Note note);

    Collection<User> findAll();

    List<Note> findAllByUser(Long id);
}

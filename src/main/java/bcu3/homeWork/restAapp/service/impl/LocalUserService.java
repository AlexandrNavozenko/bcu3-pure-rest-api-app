package bcu3.homeWork.restAapp.service.impl;

import bcu3.homeWork.restAapp.model.Note;
import bcu3.homeWork.restAapp.model.User;
import bcu3.homeWork.restAapp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LocalUserService implements UserService {

    private AtomicLong sequence = new AtomicLong(0L);

    public Map<Long, User> container = new ConcurrentHashMap<>();

    @Override
    public User getById(Long userId) {
        validateUserId(userId);

        return container.get(userId);
    }

    @Override
    public void addUser(User user) {
        if (user.getId() == null) {
            Long id = sequence.incrementAndGet();
            user.setId(id);
        }

        container.put(user.getId(), user);
    }

    @Override
    public void removeUser(Long id) {
        validateUserId(id);

        container.remove(id);
    }

    @Override
    public void addUserNote(Long userId, Note note) {
        User user = getById(userId);
        var notes = user.getNotes();
        if (notes == null) {
            notes = new ArrayList<>();
            user.setNotes(notes);
        }

        notes.add(note);
    }

    @Override
    public Collection<User> findAll() {
        return container.values();
    }

    @Override
    public List<Note> findAllByUser(Long id) {
        return Optional.of(getById(id).getNotes()).orElse(new ArrayList<>());
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new RuntimeException("ERROR: user id is empty");
        }

        if (userId < 0 || userId > sequence.get()) {
            throw new RuntimeException("ERROR: user id out of bounds");
        }
    }
}

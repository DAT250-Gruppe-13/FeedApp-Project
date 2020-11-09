package no.hvl.dat250.FeedApp.DAO;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    List<T> read();

    T read(long id);

    void create(T t);

    void update(T t);

    void delete(long id);
}

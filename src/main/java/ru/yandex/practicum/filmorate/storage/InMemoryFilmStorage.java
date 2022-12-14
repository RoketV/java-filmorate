package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.factories.FilmFactory;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Map<Long, Film> getFilms() {
        return films;
    }

    public Film postFilm(FilmDto dto) {
        if (dto == null) {
            log.warn("trying to make null film");
            throw new ValidationException("entity cannot be null");
        }
        Film film = FilmFactory.createFilm(dto);
        films.put(film.getId(), film);
        log.info("film posted");
        return film;
    }

    public Film updateFilm(FilmDto dto) {
        Film film = FilmMapper.FILM_MAPPER.toFilm(dto);
        if (film == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("film updated");
        } else {
            log.warn("cannot update non-existing entity");
            throw new NoSuchEntityException();
        }
        return film;
    }

    public Film getFilm(long id) {
        if (!films.containsKey(id)) {
            log.info("trying to get non existing film");
            throw new NoSuchEntityException();
        }
        return films.get(id);
    }

    public Film deleteFilm(FilmDto dto) {
        Film film = FilmMapper.FILM_MAPPER.toFilm(dto);
        films.remove(film.getId());
        return film;
    }
}

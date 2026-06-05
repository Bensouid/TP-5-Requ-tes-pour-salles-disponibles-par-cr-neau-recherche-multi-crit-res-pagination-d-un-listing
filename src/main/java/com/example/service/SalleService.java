package com.example.service;

import com.example.model.Salle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SalleService {

    List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end);

    List<Salle> findByCriteria(Map<String, Object> criteria);

    List<Salle> findAllPaginated(int page, int size);

    long count();

    Salle findById(Long id);

    List<Salle> findAll();

    void save(Salle salle);

    void update(Salle salle);

    void delete(Salle salle);
}
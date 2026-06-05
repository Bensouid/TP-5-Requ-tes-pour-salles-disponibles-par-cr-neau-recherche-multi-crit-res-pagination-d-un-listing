package com.example.service;

import com.example.model.Salle;
import com.example.repository.SalleRepository;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SalleServiceImpl implements SalleService {

    private EntityManager entityManager;
    private SalleRepository salleRepository;

    public SalleServiceImpl(EntityManager entityManager, SalleRepository salleRepository) {
        this.entityManager = entityManager;
        this.salleRepository = salleRepository;
    }

    @Override
    public List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return salleRepository.findAvailableRooms(start, end);
    }

    @Override
    public List<Salle> findByCriteria(Map<String, Object> criteria) {
        return salleRepository.findByCriteria(criteria);
    }

    @Override
    public List<Salle> findAllPaginated(int page, int size) {
        // Sécurité pour la pagination
        if (page < 1) page = 1;
        return salleRepository.findAllPaginated(page, size);
    }

    @Override
    public long count() {
        return salleRepository.count();
    }

    @Override
    public Salle findById(Long id) {
        return salleRepository.findById(id);
    }

    @Override
    public List<Salle> findAll() {
        return salleRepository.findAll();
    }

    @Override
    public void save(Salle salle) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            salleRepository.save(salle);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void update(Salle salle) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            salleRepository.update(salle);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(Salle salle) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            salleRepository.delete(salle);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
package com.example.repository;

import com.example.model.Salle;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalleRepositoryImpl implements SalleRepository {

    private final EntityManager entityManager;

    public SalleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // 1. Recherche de salles disponibles par créneau (Requête JPQL)
    @Override
    public List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end) {
        String jpql = "SELECT DISTINCT s FROM Salle s WHERE s.id NOT IN " +
                "(SELECT r.salle.id FROM Reservation r " +
                "WHERE r.dateDebut < :end AND r.dateFin > :start)";

        return entityManager.createQuery(jpql, Salle.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    // 2. Recherche multi-critères dynamique (API Criteria JPA)
    @Override
    public List<Salle> findByCriteria(Map<String, Object> criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Salle> query = cb.createQuery(Salle.class);
        Root<Salle> salle = query.from(Salle.class);

        List<Predicate> predicates = new ArrayList<>();

        if (criteria != null) {
            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "capaciteMin":
                        predicates.add(cb.ge(salle.get("capacite"), (Number) value));
                        break;
                    case "capaciteMax":
                        predicates.add(cb.le(salle.get("capacite"), (Number) value));
                        break;
                    case "batiment":
                        predicates.add(cb.equal(salle.get("batiment"), value.toString()));
                        break;
                    case "etage":
                        predicates.add(cb.equal(salle.get("etage"), (Number) value));
                        break;
                }
            }
        }

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    // 3. Pagination (Requête JPQL avec setFirstResult et setMaxResults)
    @Override
    public List<Salle> findAllPaginated(int page, int size) {
        return entityManager.createQuery("SELECT s FROM Salle s ORDER BY s.id", Salle.class)
                .setFirstResult((page - 1) * size) // Calcul de l'index de départ (offset)
                .setMaxResults(size)              // Nombre d'éléments max par page (limit)
                .getResultList();
    }

    // 4. Compter le nombre total d'éléments (Utile pour la pagination)
    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(s) FROM Salle s", Long.class)
                .getSingleResult();
    }

    // --- Méthodes CRUD de Base ---

    @Override
    public Salle findById(Long id) {
        return entityManager.find(Salle.class, id);
    }

    @Override
    public List<Salle> findAll() {
        return entityManager.createQuery("SELECT s FROM Salle s", Salle.class).getResultList();
    }

    @Override
    public void save(Salle salle) {
        entityManager.persist(salle);
    }

    @Override
    public void update(Salle salle) {
        entityManager.merge(salle);
    }

    @Override
    public void delete(Salle salle) {
        // Gestion de la suppression en s'assurant que l'entité est rattachée (managed)
        if (entityManager.contains(salle)) {
            entityManager.remove(salle);
        } else {
            entityManager.remove(entityManager.merge(salle));
        }
    }
}
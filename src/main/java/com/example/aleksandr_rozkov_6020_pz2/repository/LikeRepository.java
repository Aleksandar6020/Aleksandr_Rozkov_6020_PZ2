package com.example.aleksandr_rozkov_6020_pz2.repository;

import com.example.aleksandr_rozkov_6020_pz2.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndManulId(Long userId, Long manulId);
    Optional<Like> findByUserIdAndManulId(Long userId, Long manulId);
    long countByManulId(Long manulId);
    List<Like> findByManulId(Long manulId);
    void deleteByManulId(Long manulId);
}

package com.example.aleksandr_rozkov_6020_pz2.repository;

import com.example.aleksandr_rozkov_6020_pz2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
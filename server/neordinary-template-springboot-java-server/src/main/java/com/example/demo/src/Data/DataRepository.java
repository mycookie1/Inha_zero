package com.example.demo.src.Data;

import com.example.demo.src.Data.Entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataRepository extends JpaRepository<Data,Long> {
    List<Data> findByNameContaining(String name);
}

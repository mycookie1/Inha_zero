package com.example.demo.src.tree;

import com.example.demo.src.tree.entity.Tree;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeRepository extends JpaRepository<Tree,Long> {
    List<Tree> findAllByUserId(User userId);
}

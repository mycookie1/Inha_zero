package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.tree.entity.Tree;
import com.example.demo.src.treesImage.entity.TreesImage;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "User") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class User extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = true)
    private String account;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private Integer score;

    @OneToMany(mappedBy = "userId")
    private List<Tree> trees = new ArrayList<>();

    @OneToMany(mappedBy = "userId")
    private List<TreesImage> treesImage = new ArrayList<>();

    @Builder
    public User(Long userId, String account, String email, String password, Integer score) {
        this.userId = userId;
        this.account = account;
        this.email = email;
        this.password = password;
        this.score = score;
    }
}

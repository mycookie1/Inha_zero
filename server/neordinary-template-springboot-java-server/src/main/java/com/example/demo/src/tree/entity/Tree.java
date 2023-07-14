package com.example.demo.src.tree.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "Tree") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Tree extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treeId;

    @Column(nullable = true)
    private Integer size;
    @Column(nullable = true)
    private Integer color;

    @Column(nullable = true)
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User userId;

    @Builder
    public Tree(Integer size,Integer color, Integer score) {
        this.size = size;
        this.color = color;
        this.score = score;
    }
}

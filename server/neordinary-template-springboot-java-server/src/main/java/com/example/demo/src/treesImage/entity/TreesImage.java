package com.example.demo.src.treesImage.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "TreesImage") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class TreesImage extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treesImageId;

    @Column(nullable = true)
    private String url;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;

    @Builder
    public TreesImage(Long treesImageId, String url) {
        this.treesImageId = treesImageId;
        this.url = url;
    }
}

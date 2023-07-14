package com.example.demo.src.Data.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "Data") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Data {
    @Id // PK를 의미하는 어노테이션
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long index;

    @Column(nullable = true)
    private String category;
    @Column(nullable = true)
    private String name;
}

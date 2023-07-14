package com.example.demo.src.user.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetTrees {
    @Schema(name = "totalScore", example = "140", description = "자신의 전체 점수")
    private Integer totalScore;

    @Schema(name = "score", example = "10", description = "현재 키우고있는 나무의 점수")
    private Integer score;
    @Schema(name = "treeList", example = "[{크기, 색},{크기, 색}]", description = "거목들의 정보")
    private List<GetOneTree> treeList;
}


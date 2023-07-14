package com.example.demo.src.user.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetOneTree {
    @Schema(name = "size", example = "1", description = "1:씨앗 2:중간 3:거목")
    private Integer size;
    @Schema(name = "color", example = "2", description = "1: 2: 3: ")
    private Integer color;

}

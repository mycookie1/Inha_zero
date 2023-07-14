package com.example.demo.src.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Images {
    private String format = "png";
    private String name = "medium";
    private String data = null;
    private String url = "";

}

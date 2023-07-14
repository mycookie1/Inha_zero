package com.example.demo.src.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Clova {
    Images images;
    String lang = "ko";
    String requestId = "string";
    String resultType = "string";
    String timestamp = null;
    String version = "V1";
}

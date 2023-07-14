package com.example.demo.src.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetReceipt {
    String name;
    String category;
    Integer score = 0;
    Integer count=0;
}

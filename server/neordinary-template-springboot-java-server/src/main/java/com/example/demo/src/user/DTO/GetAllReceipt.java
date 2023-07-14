package com.example.demo.src.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetAllReceipt {
    List<GetReceipt> getReceipts;
    Integer totalScore;
}

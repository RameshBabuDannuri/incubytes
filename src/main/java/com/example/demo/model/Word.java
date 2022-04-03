package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Words")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Word {
    @Id
    @GeneratedValue
    private Long id;
    private String word;

}

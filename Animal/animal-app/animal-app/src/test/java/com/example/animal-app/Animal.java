package com.example.animalapp.model;

import javax.persistence.*;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    
    @Lob
    private byte[] image;
    private String imageType;

    private String description;
    private String lifeExpectancy;

    // Getters and Setters
}

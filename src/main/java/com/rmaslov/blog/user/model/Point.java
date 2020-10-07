package com.rmaslov.blog.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.swing.text.Document;

@Getter
@Setter
@Builder
@ToString
public class Point {
    private Double lat;
    private Double lng;
}

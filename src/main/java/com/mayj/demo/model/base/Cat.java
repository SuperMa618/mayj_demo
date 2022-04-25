package com.mayj.demo.model.base;

import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @ClassName Cat
 * @Description
 * @Author Mayj
 * @Date 2022/4/25 19:05
 **/
@Data
@Service
public class Cat {
    private String catName;
    public Cat setCatName(String name) {
        this.catName = name;
        return this;
    }
}

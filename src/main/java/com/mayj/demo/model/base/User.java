package com.mayj.demo.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName User
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/12 14:55
 **/

@Data
@TableName("user1")
public class User {
    @TableId(value = "id")
    private int id;

    @TableField(value = "name")
    private String name;
}

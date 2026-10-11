package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果封装类
 * 分页接口统一返回：{ total: 总记录数, rows: 当前页数据 }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total;         // 总记录数
    private List<T> rows;       // 当前页数据列表
}

package xyz.liuyuhe.demo.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求参数
 */

@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = -9081656336751681819L;
    /**
     * 当前页号
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;
}

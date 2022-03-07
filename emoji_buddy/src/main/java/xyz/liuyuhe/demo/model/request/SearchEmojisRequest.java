package xyz.liuyuhe.demo.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchEmojisRequest extends PageRequest{
    private String keyword;
}

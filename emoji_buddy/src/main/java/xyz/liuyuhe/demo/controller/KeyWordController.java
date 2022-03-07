package xyz.liuyuhe.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.liuyuhe.demo.common.BaseResponse;
import xyz.liuyuhe.demo.common.Result;
import xyz.liuyuhe.demo.model.entity.KeyWord;
import xyz.liuyuhe.demo.service.KeyWordService;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/keyword")
public class KeyWordController {
    @Autowired
    private KeyWordService keyWordService;

    @GetMapping("/hot")
    public BaseResponse<List<KeyWord>> getHotKeyWords(@RequestParam(defaultValue = "5", required = false) int n) {
        return Result.success(keyWordService.hotTopK(n));
    }
}

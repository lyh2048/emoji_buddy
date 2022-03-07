package xyz.liuyuhe.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.liuyuhe.demo.common.BaseResponse;
import xyz.liuyuhe.demo.common.Result;
import xyz.liuyuhe.demo.enums.ErrorCodeEnum;
import xyz.liuyuhe.demo.exception.BusinessException;
import xyz.liuyuhe.demo.job.AsyncTask;
import xyz.liuyuhe.demo.model.entity.Emoji;
import xyz.liuyuhe.demo.model.entity.KeyWord;
import xyz.liuyuhe.demo.model.request.SearchEmojisRequest;
import xyz.liuyuhe.demo.service.EmojiService;
import xyz.liuyuhe.demo.service.KeyWordService;

import java.util.Date;


@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/emoji")
public class EmojiController {
    @Autowired
    private EmojiService emojiService;
    @Autowired
    private KeyWordService keyWordService;
    @Autowired
    private AsyncTask asyncTask;

    @GetMapping("/search")
    public BaseResponse<Page<Emoji>> searchEmoji(SearchEmojisRequest emojiRequest) {
        if (emojiRequest == null) {
            throw new BusinessException(ErrorCodeEnum.REQUEST_PARAMS_ERROR);
        }
        if (StringUtils.isBlank(emojiRequest.getKeyword())) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAMS_ERROR.getCode(), "关键字不能空");
        }
        final int MAX_TEXT_NUM = 50;
        if (emojiRequest.getKeyword().length() > MAX_TEXT_NUM) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAMS_ERROR.getCode(), "关键字太长了");
        }
        boolean flag = keyWordService.KeyWordExist(emojiRequest.getKeyword());
        if (flag) {
            // 增加搜索权重
            KeyWord keyWord = keyWordService.findKeyWordByContent(emojiRequest.getKeyword());
            keyWord.setWeight(keyWord.getWeight() + 1);
            keyWord.setUpdateTime(new Date());
            keyWordService.updateKeyWordWeight(keyWord);
        } else {
            // 写入数据库
            KeyWord keyWord = new KeyWord()
                    .setContent(emojiRequest.getKeyword())
                    .setWeight(0L)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
            keyWordService.saveKeyWord(keyWord);
            // 爬取数据
            asyncTask.spiderData(emojiRequest.getKeyword());
        }
        int pageNum = emojiRequest.getPageNum();
        int pageSize = emojiRequest.getPageSize();
        final int MAX_VIEW_NUM = 1000;
        if (pageNum * pageSize > MAX_VIEW_NUM) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAMS_ERROR.getCode(), "最多可查看 1000 条数据");
        }
        return Result.success(emojiService.searchEmoji(emojiRequest.getKeyword(), pageNum, pageSize));
    }
}

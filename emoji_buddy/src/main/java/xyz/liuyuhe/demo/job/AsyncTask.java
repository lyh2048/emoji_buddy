package xyz.liuyuhe.demo.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.liuyuhe.demo.crawler.CrawlerTemplate;
import xyz.liuyuhe.demo.crawler.FaBiaoQing;
import xyz.liuyuhe.demo.model.entity.Emoji;
import xyz.liuyuhe.demo.service.EmojiService;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class AsyncTask {
    @Autowired
    private EmojiService emojiService;

    @SneakyThrows
    @Async
    public void spiderData(String keyword) {
        FaBiaoQing f = new FaBiaoQing();
        for (int i = f.getStartPage(); i <= f.getMaxPage(); i++) {
            log.debug("page {} start", i);
            List<CrawlerTemplate.Picture> pictureList = f.doCrawler(keyword, i);
            for (CrawlerTemplate.Picture picture : pictureList) {
                Emoji emoji = new Emoji()
                        .setTitle(picture.getTitle())
                        .setUrl(picture.getImageUrl())
                        .setStatus(1)
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date());
                emoji = emojiService.saveEmoji(emoji);
                log.debug(emoji.toString());
            }
            log.debug("page {} end", i);
        }
    }
}

package xyz.liuyuhe.demo.crawler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.liuyuhe.demo.utils.HttpUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FaBiaoQing extends CrawlerTemplate {

    public FaBiaoQing() {
        super("发表情", 1, 10);
    }

    @Override
    public List<Picture> doCrawler(String keyword, int pageNum) {
        List<Picture> result = new ArrayList<>();
        String body = null;
        try {
            body = HttpUtils.doGet(String.format("https://www.fabiaoqing.com/search/bqb/keyword/%s/type/bq/page/%s.html", keyword, pageNum), null);
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }
        if (StringUtils.isNotBlank(body)) {
            Document document = Jsoup.parse(body);
            Elements elements = document.select("img[class=ui image bqppsearch lazy]");
            for (Element element : elements) {
                String title = element.attr("title");
                String url = element.attr("data-original");
                Picture picture = new Picture();
                picture.setTitle(title);
                picture.setImageUrl(url);
                result.add(picture);
            }
        }
        return result;
    }
}

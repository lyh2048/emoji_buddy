package xyz.liuyuhe.demo.crawler;

import lombok.Data;

import java.util.List;

public abstract class CrawlerTemplate {
    private final String name;
    private final int startPage;
    private final int maxPage;

    public String getName() {
        return name;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    protected CrawlerTemplate(String name, int startPage, int maxPage) {
        this.name = name;
        this.startPage = startPage;
        this.maxPage = maxPage;
    }

    @Data
    public static final class Picture {
        private String title;
        private String imageUrl;
    }

    @Data
    public static final class PictureResponse {
        private List<Picture> data;
    }

    public abstract List<Picture> doCrawler(String keyword, int pageNum);
}

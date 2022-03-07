package xyz.liuyuhe.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import xyz.liuyuhe.demo.model.entity.Emoji;

import java.util.List;

@Service
public class EmojiService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Emoji saveEmoji(Emoji emoji) {
        return mongoTemplate.insert(emoji);
    }

    public Page<Emoji> searchEmoji(String keyword, int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        List<Emoji> emojiList = mongoTemplate.find(Query.query(Criteria.where("title").is(keyword).regex(keyword).and("status").is(1)).with(pageable), Emoji.class);
        long count = mongoTemplate.count(Query.query(Criteria.where("title").is(keyword).regex(keyword).and("status").is(1)), Emoji.class);
        return new PageImpl<>(emojiList, pageable, count);
    }
}

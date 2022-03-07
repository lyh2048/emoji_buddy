package xyz.liuyuhe.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import xyz.liuyuhe.demo.model.entity.KeyWord;

import java.util.List;

@Service
public class KeyWordService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public KeyWord saveKeyWord(KeyWord keyWord) {
        return mongoTemplate.insert(keyWord);
    }

    public void updateKeyWordWeight(KeyWord keyWord) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("content").is(keyWord.getContent()).regex(keyWord.getContent())), Update.update("weight", keyWord.getWeight()), KeyWord.class);
    }

    public KeyWord findKeyWordByContent(String content) {
        return mongoTemplate.findOne(Query.query(Criteria.where("content").is(content).regex(content)), KeyWord.class);
    }

    public boolean KeyWordExist(String content) {
        return mongoTemplate.exists(Query.query(Criteria.where("content").is(content).regex(content)), KeyWord.class);
    }

    public List<KeyWord> hotTopK(int n) {
        Sort sort = Sort.by(Sort.Direction.DESC, "weight");
        return mongoTemplate.find(Query.query(Criteria.where("weight").gt(0)).with(sort).limit(n), KeyWord.class);
    }
}

package ge.market.flea.service;import java.util.ArrayList;import java.util.Collection;import java.util.List;import java.util.stream.Collectors;import com.mongodb.client.result.InsertOneResult;import org.bson.Document;import org.bson.conversions.Bson;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.data.mongodb.core.MongoTemplate;import org.springframework.stereotype.Service;import com.mongodb.client.MongoCollection;import com.mongodb.client.model.Filters;import static com.mongodb.client.model.Filters.*;import ge.market.flea.data.entity.Goods;import ge.market.flea.data.entity.GoodsRepository;import ge.market.flea.util.Mapper;@Servicepublic class MobileService {    @Autowired    private GoodsRepository goodsRepository;    @Autowired    private MongoTemplate mongoTemplate;    public List<Goods> find(String mobile, Integer page) {        MongoCollection mongoCollection = mongoTemplate.getCollection("goods");        //Bson bson = eq("goodName", mobile);       // Bson regex =  Filters.regex("goodName", mobile);        Document query = new Document("goodName", new Document("$regex", mobile).append("$options", "i"));     Collection<Document> documents =  mongoCollection.find(query).skip(page).limit(10)         .into(new ArrayList());        return documents.stream().map(Mapper::mapToGoods).collect(Collectors.toList());    }    public void save(Goods goods) {        MongoCollection mongoCollection = mongoTemplate.getCollection("goods");        InsertOneResult documents =  mongoCollection.insertOne(new Document()                .append("goodName", goods.getGoodName())                .append("description", goods.getDescription())                .append("nameSplitted", null)                .append("tags", null));        mongoCollection.insertOne(documents);    }}
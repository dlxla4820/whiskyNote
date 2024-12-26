package develop.whiskyNote.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.WhiskyItemDto;
import develop.whiskyNote.entity.Whisky;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static develop.whiskyNote.entity.QWhisky.whisky;

@Repository
public class GlobalWhiskyRepository {
    private final WhiskyRepository whiskyRepository;
    private final JPAQueryFactory queryFactory;


    public GlobalWhiskyRepository(
            WhiskyRepository whiskyRepository,
            JPAQueryFactory queryFactory
            ) {
        this.whiskyRepository = whiskyRepository;
        this.queryFactory = queryFactory;
    }
    private Integer pageSize = 10;

    /**
     * DB SELECT :
     * **/
    public List<Whisky> getWhiskeis(String whiskyName, Integer pageNum) {
        NumberExpression<Integer> accuracy = new CaseBuilder()
                .when(whisky.whiskyName.eq(whiskyName)).then(1)
                .when(whisky.whiskyName.startsWith(whiskyName)).then(2)
                .when(whisky.whiskyName.contains(whiskyName)).then(3)
                .otherwise(4);
        var query =  queryFactory.selectFrom(whisky)
                .where(whisky.whiskyName.contains(whiskyName))
                .orderBy(accuracy.asc(), whisky.whiskyName.asc());
        int totalPages = (int)Math.ceil((double)query.fetchCount()/pageSize);
        if(pageNum>totalPages) pageNum = totalPages;
        return query.offset((long) (pageNum - 1) *pageSize).limit(pageSize).fetch();
    }

    /**
     * DB INSERT : WHISKY
     * **/
    public List<Whisky> saveWhiskies(List<WhiskyItemDto> whiskyItems) {
        List<Whisky> whiskyList = new ArrayList<>();
        whiskyItems.forEach(whiskyItem -> {whiskyList.add(Whisky.builder()
                        .botteledYear(whiskyItem.getYear())
                        .whiskyName(whiskyItem.getName())
                        .imageUrl(whiskyItem.getImage())
                        .caskNumber(whiskyItem.getCaskType())
                        .whiskyCategory(whiskyItem.getCategory())
                        .strength(whiskyItem.getStrength())
                .build());});
        return whiskyRepository.saveAll(whiskyList);
    }
}

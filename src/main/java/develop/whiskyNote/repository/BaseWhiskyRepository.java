package develop.whiskyNote.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.BaseWhiskyRequestDto;
import develop.whiskyNote.entity.BasicWhiskyInfo;
import develop.whiskyNote.entity.Whisky;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static develop.whiskyNote.entity.QWhisky.whisky;
import static develop.whiskyNote.entity.QBasicWhiskyInfo.basicWhiskyInfo;

@Repository
public class BaseWhiskyRepository {
    private final WhiskyRepository whiskyRepository;
    private final JPAQueryFactory queryFactory;
    private final BasicWhiskyInfoRepository basicWhiskyInfoRepository;


    public BaseWhiskyRepository(
            WhiskyRepository whiskyRepository,
            BasicWhiskyInfoRepository basicWhiskyInfoRepository,
            JPAQueryFactory queryFactory
            ) {
        this.whiskyRepository = whiskyRepository;
        this.queryFactory = queryFactory;
        this.basicWhiskyInfoRepository = basicWhiskyInfoRepository;
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
    public List<BasicWhiskyInfo> saveWhiskies(BaseWhiskyRequestDto baseWhiskyRequestDtos) {
        List<BasicWhiskyInfo> whiskyList = new ArrayList<>();
        baseWhiskyRequestDtos.getWhiskyList().forEach(inputWhiskyDto -> {
            whiskyList.add(BasicWhiskyInfo.builder()
                    .koreaName(inputWhiskyDto.getKoreaName())
                    .englishName(inputWhiskyDto.getEnglishName())
                    .country(inputWhiskyDto.getCountry())
                    .build());

        });

        return basicWhiskyInfoRepository.saveAll(whiskyList);
    }

    public Set<String> findExistingWhiskyNames(BaseWhiskyRequestDto baseWhiskyRequestDtos) {
        return queryFactory
                .select(basicWhiskyInfo.koreaName, basicWhiskyInfo.englishName)
                .from(basicWhiskyInfo)
                .where(
                        basicWhiskyInfo.koreaName.in(baseWhiskyRequestDtos.getWhiskyList().stream().map(BaseWhiskyRequestDto.inputWhiskyDto::getKoreaName).collect(Collectors.toSet()))
                                .or(basicWhiskyInfo.englishName.in(baseWhiskyRequestDtos.getWhiskyList().stream().map(BaseWhiskyRequestDto.inputWhiskyDto::getEnglishName).collect(Collectors.toSet())))
                )
                .fetch()
                .stream()
                .flatMap(t -> List.of(t.get(basicWhiskyInfo.koreaName), t.get(basicWhiskyInfo.englishName)).stream())
                .collect(Collectors.toSet());
    }
    public List<BasicWhiskyInfo> getAllBasicWhiskyInfos() {
        return basicWhiskyInfoRepository.findAll();
    }


}

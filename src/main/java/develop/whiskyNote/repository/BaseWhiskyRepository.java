package develop.whiskyNote.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.BaseWhiskyFiveResponseDto;
import develop.whiskyNote.dto.BaseWhiskyRequestDto;
import develop.whiskyNote.entity.Whisky;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static develop.whiskyNote.entity.QWhisky.whisky;

@Repository
public class BaseWhiskyRepository {
    private final WhiskyRepository whiskyRepository;
    private final JPAQueryFactory queryFactory;


    public BaseWhiskyRepository(
            WhiskyRepository whiskyRepository,
            JPAQueryFactory queryFactory
            ) {
        this.whiskyRepository = whiskyRepository;
        this.queryFactory = queryFactory;
    }
    private Integer pageSize = 10;

    public List<BaseWhiskyFiveResponseDto> getWhiskyByKoreaName(String keyword) {
        List<BaseWhiskyFiveResponseDto> whiskyList = queryFactory.select(Projections.fields(BaseWhiskyFiveResponseDto.class,
                        whisky.koreaName.as("whiskyKoreaName"),
                        whisky.englishName.as("whiskyEnglishName"),
                        whisky.category.as("whiskyCategory"),
                        whisky.uuid.as("whiskyUuid")
                        ))
                .from(whisky)
                .where(whisky.koreaName.contains(keyword))
                .orderBy(
                        Expressions.stringTemplate(
                                "CASE WHEN {0} LIKE {1} THEN 0 ELSE 1 END",
                                whisky.koreaName, keyword + "%"
                                ).asc(),
                        whisky.koreaName.asc()
                )
                .limit(5).fetch();
        return whiskyList;
    }
    //나중에 시간이 된다면 유사검색 시도
    public List<BaseWhiskyFiveResponseDto> getWhiskyByEnglishName(String keyword) {
        List<BaseWhiskyFiveResponseDto> whiskyList = queryFactory.select(Projections.fields(BaseWhiskyFiveResponseDto.class,
                        whisky.englishName.as("whiskyEnglishName"),
                        whisky.koreaName.as("whiskyKoreaName"),
                        whisky.category.as("whiskyCategory"),
                        whisky.uuid.as("whiskyUuid")
                ))
                .from(whisky)
                .where(whisky.englishName.containsIgnoreCase(keyword))
                .orderBy(
                        Expressions.stringTemplate(
                                "CASE WHEN {0} LIKE {1} THEN 0 ELSE 1 END",
                                whisky.englishName, keyword+"%"
                        ).asc(),
                        whisky.englishName.asc()
                )
                .limit(5).fetch();
        return whiskyList;
    }
    //Whisky Database에 데이터 추가
    public List<Whisky> saveWhiskies(BaseWhiskyRequestDto baseWhiskyRequestDtos) {
        List<Whisky> whiskyList = new ArrayList<>();
        baseWhiskyRequestDtos.getWhiskyList().forEach(InputWhiskyDTO -> {
            whiskyList.add(Whisky.builder()
                    .koreaName(InputWhiskyDTO.getKoreaName())
                    .englishName(InputWhiskyDTO.getEnglishName())
                    .country(InputWhiskyDTO.getCountry())
                    .category(InputWhiskyDTO.getCategory())
                    .strength(InputWhiskyDTO.getStrength())
                    .build());
        });

        return whiskyRepository.saveAll(whiskyList);
    }
    //기본 Whisky Entity에서 한글 이름이 같거나 영어 이름이 같은 위스키 목록을 전부 가져다 줌
    //위스키 저장시에 사용
    public Set<String> findSameKoreaNameOrEnglishName(BaseWhiskyRequestDto baseWhiskyRequestDtos) {
        return queryFactory
                .select(whisky.koreaName, whisky.englishName)
                .from(whisky)
                .where(
                    whisky.koreaName.in(baseWhiskyRequestDtos.getWhiskyList().stream().map(BaseWhiskyRequestDto.InputWhiskyDTO::getKoreaName).collect(Collectors.toSet()))
                    .or(whisky.englishName.in(baseWhiskyRequestDtos.getWhiskyList().stream().map(BaseWhiskyRequestDto.InputWhiskyDTO::getEnglishName).collect(Collectors.toSet())))
                )
                .fetch()
                .stream()
                .flatMap(t -> List.of(t.get(whisky.koreaName), t.get(whisky.englishName)).stream())
                .collect(Collectors.toSet());
    }
    //모든 위스키 목록 반환
    public List<Whisky> getAllBasicWhiskyInfos() {
        return whiskyRepository.findAll();
    }
}

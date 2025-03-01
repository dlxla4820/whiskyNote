package develop.whiskyNote.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.ReviewUpsertRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static develop.whiskyNote.entity.QImageFile.imageFile;

@Repository
@RequiredArgsConstructor
public class ImageFileDetailRepository {
    private final ImageFileRepository imageFileRepository;
    private final JPAQueryFactory queryFactory;

    public void updateImageFileIsSavedByNameAndUserUuid(String name, UUID userUuid){
        queryFactory.update(imageFile)
                .set(imageFile.isSaved, true)
                .where(imageFile.name.eq(name))
                .where(imageFile.userUuid.eq(userUuid))
                .execute();
    }

}


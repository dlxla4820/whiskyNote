package develop.whiskyNote.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewLikeMapping is a Querydsl query type for ReviewLikeMapping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewLikeMapping extends EntityPathBase<ReviewLikeMapping> {

    private static final long serialVersionUID = -2092571946L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewLikeMapping reviewLikeMapping = new QReviewLikeMapping("reviewLikeMapping");

    public final ComparablePath<java.util.UUID> reviewId = createComparable("reviewId", java.util.UUID.class);

    public final QUser user;

    public final ComparablePath<java.util.UUID> uuid = createComparable("uuid", java.util.UUID.class);

    public QReviewLikeMapping(String variable) {
        this(ReviewLikeMapping.class, forVariable(variable), INITS);
    }

    public QReviewLikeMapping(Path<? extends ReviewLikeMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewLikeMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewLikeMapping(PathMetadata metadata, PathInits inits) {
        this(ReviewLikeMapping.class, metadata, inits);
    }

    public QReviewLikeMapping(Class<? extends ReviewLikeMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}


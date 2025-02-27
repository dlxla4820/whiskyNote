package develop.whiskyNote.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1066926175L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final StringPath content = createString("content");

    public final MapPath<Long, String, StringPath> imageUrl = this.<Long, String, StringPath>createMap("imageUrl", Long.class, String.class, StringPath.class);

    public final BooleanPath isAnonymous = createBoolean("isAnonymous");

    public final DateTimePath<java.time.LocalDateTime> modDate = createDateTime("modDate", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> openDate = createDate("openDate", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> score = createNumber("score", Long.class);

    public final ListPath<String, StringPath> tags = this.<String, StringPath>createList("tags", String.class, StringPath.class, PathInits.DIRECT2);

    public final QUser user;

    public final QUserWhisky userWhisky;

    public final ComparablePath<java.util.UUID> uuid = createComparable("uuid", java.util.UUID.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.userWhisky = inits.isInitialized("userWhisky") ? new QUserWhisky(forProperty("userWhisky"), inits.get("userWhisky")) : null;
    }

}


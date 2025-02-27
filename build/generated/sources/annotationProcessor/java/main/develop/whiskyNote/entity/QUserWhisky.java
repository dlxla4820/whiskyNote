package develop.whiskyNote.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserWhisky is a Querydsl query type for UserWhisky
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserWhisky extends EntityPathBase<UserWhisky> {

    private static final long serialVersionUID = 493225437L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserWhisky userWhisky = new QUserWhisky("userWhisky");

    public final NumberPath<Integer> bottledYear = createNumber("bottledYear", Integer.class);

    public final StringPath caskType = createString("caskType");

    public final StringPath category = createString("category");

    public final StringPath country = createString("country");

    public final StringPath englishName = createString("englishName");

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath koreaName = createString("koreaName");

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.time.LocalDateTime> modDate = createDateTime("modDate", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> openDate = createDate("openDate", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final NumberPath<Double> strength = createNumber("strength", Double.class);

    public final ComparablePath<java.util.UUID> userUuid = createComparable("userUuid", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> uuid = createComparable("uuid", java.util.UUID.class);

    public final QWhisky whisky;

    public QUserWhisky(String variable) {
        this(UserWhisky.class, forVariable(variable), INITS);
    }

    public QUserWhisky(Path<? extends UserWhisky> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserWhisky(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserWhisky(PathMetadata metadata, PathInits inits) {
        this(UserWhisky.class, metadata, inits);
    }

    public QUserWhisky(Class<? extends UserWhisky> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.whisky = inits.isInitialized("whisky") ? new QWhisky(forProperty("whisky")) : null;
    }

}


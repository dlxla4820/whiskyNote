package develop.whiskyNote.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBackupCode is a Querydsl query type for BackupCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBackupCode extends EntityPathBase<BackupCode> {

    private static final long serialVersionUID = -2136628584L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBackupCode backupCode = new QBackupCode("backupCode");

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser user;

    public QBackupCode(String variable) {
        this(BackupCode.class, forVariable(variable), INITS);
    }

    public QBackupCode(Path<? extends BackupCode> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBackupCode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBackupCode(PathMetadata metadata, PathInits inits) {
        this(BackupCode.class, metadata, inits);
    }

    public QBackupCode(Class<? extends BackupCode> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}


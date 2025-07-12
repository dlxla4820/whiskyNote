package develop.whiskyNote.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBackupCode is a Querydsl query type for BackupCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBackupCode extends EntityPathBase<BackupCode> {

    private static final long serialVersionUID = -2136628584L;

    public static final QBackupCode backupCode = new QBackupCode("backupCode");

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ComparablePath<java.util.UUID> userUuid = createComparable("userUuid", java.util.UUID.class);

    public QBackupCode(String variable) {
        super(BackupCode.class, forVariable(variable));
    }

    public QBackupCode(Path<? extends BackupCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBackupCode(PathMetadata metadata) {
        super(BackupCode.class, metadata);
    }

}


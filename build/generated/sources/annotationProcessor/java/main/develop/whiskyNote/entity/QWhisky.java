package develop.whiskyNote.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWhisky is a Querydsl query type for Whisky
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWhisky extends EntityPathBase<Whisky> {

    private static final long serialVersionUID = -921387342L;

    public static final QWhisky whisky = new QWhisky("whisky");

    public final StringPath category = createString("category");

    public final StringPath country = createString("country");

    public final StringPath englishName = createString("englishName");

    public final StringPath koreaName = createString("koreaName");

    public final NumberPath<Double> strength = createNumber("strength", Double.class);

    public final ComparablePath<java.util.UUID> uuid = createComparable("uuid", java.util.UUID.class);

    public QWhisky(String variable) {
        super(Whisky.class, forVariable(variable));
    }

    public QWhisky(Path<? extends Whisky> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWhisky(PathMetadata metadata) {
        super(Whisky.class, metadata);
    }

}


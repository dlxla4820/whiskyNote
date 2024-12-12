package develop.whiskyNote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

import static develop.whiskyNote.entity.QUser.user;

@Repository
public class UserInfoRepository {
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;

    public UserInfoRepository(UserRepository userRepository, JPAQueryFactory queryFactory) {
        this.userRepository = userRepository;
        this.queryFactory = queryFactory;
    }
    /**
     * DB INSERT : USER
     **/
    public User saveUser(UserRequestDto userRequestDto){
        User user = User.builder()
                .deviceId(userRequestDto.getDeviceId())
                .regDate(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

    /**
     * DB SELECT :
     **/

    public User getUserByUuid(UUID uuid){
        return  queryFactory.selectFrom(user)
                .where(user.uuid.eq(uuid))
                .fetchOne();
    }

    public UUID getUuidByDeviceId(String deviceId){
        return  queryFactory.select(user.uuid)
                .from(user)
                .where(user.deviceId.eq(deviceId))
                .fetchOne();
    }

}

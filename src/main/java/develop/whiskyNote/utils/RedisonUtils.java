package develop.whiskyNote.utils;

import develop.whiskyNote.exception.RedissonException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RedisonUtils {
    private final RedissonClient redissonClient;

    public RedisonUtils( RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        String lockKey = getLockKey(joinPoint, redissonLock.key());
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try{
            isLocked = lock.tryLock(redissonLock.waitTime(),redissonLock.leaseTime(), TimeUnit.SECONDS);
            if(!isLocked){
                throw new RedissonException("REDISSON_ENTITY_LOCK");
            }
            return joinPoint.proceed();//원래 메서드 실행
        } finally {
            if(isLocked && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

    private String getLockKey(ProceedingJoinPoint joinPoint,String keyExpression){
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(keyExpression.replace("#", ""))) {
                return args[i].toString();
            }
        }
        throw new RedissonException("REDISSON_WRONG_KEY");
    }
}

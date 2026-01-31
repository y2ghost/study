package study.ywork.users.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import study.ywork.users.domain.RetroBoard;
import study.ywork.users.exception.RetroBoardNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Component
@Aspect
public class RetroBoardAdvice {
    private final Logger log = LoggerFactory.getLogger(RetroBoardAdvice.class);

    @Around("execution(* study.ywork.users.repository.RetroBoardRepository.findById(..))")
    public Object checkFindRetroBoard(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("[ADVICE] {}", proceedingJoinPoint.getSignature().getName());
        Optional<RetroBoard> retroBoard = (Optional<RetroBoard>) proceedingJoinPoint.proceed(new Object[]{
                UUID.fromString(proceedingJoinPoint.getArgs()[0].toString())
        });

        if (retroBoard.isEmpty()) {
            throw new RetroBoardNotFoundException();
        }

        return retroBoard;
    }
}

package com.megait.mymall.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class SaveLog {

    @AfterReturning("execution(* com.megait.mymall.repository.*Repository.save*(..))")
    void log(){

        log.info("엔티티 추가됨!");
    }
    @Before("execution(String com.megait.mymall.controller.MainController..*(..))")
    void log2(){
        log.info("--요청 실행됨--");
    }
}

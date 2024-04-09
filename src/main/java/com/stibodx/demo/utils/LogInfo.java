package com.stibodx.demo.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.UUID;
@Data
@Slf4j
public class LogInfo {
    private String clientName;
    private String uuid;
    private String className;
    public LogInfo(Class<?> classForLog, String clientName) {
        this.className = classForLog.getCanonicalName();
        this.clientName = clientName;
        this.uuid = UUID.randomUUID().toString();
    }
    public LogInfo(Class<?> classForLog, LogInfo fatherLogInfo) {
        this.className = classForLog.getCanonicalName();
        this.clientName = fatherLogInfo.getClientName();
        this.uuid = fatherLogInfo.getUuid();
    }
    public void generateInfoLog(String... msg) {
        log.info(buildMsg(msg) + " User: " + this.getClientName() + " UUID: " + this.getUuid());
    }
    public void generateErrorLog(String... msg) {
        log.error(buildMsg(msg) + "User: " + this.getClientName() + " UUID: " + this.getUuid());
    }

    public String buildMsg(String... msg) {
        return Arrays.stream(msg)
                .map(message -> message + ", ")
                .reduce("", String::concat);
    }

}

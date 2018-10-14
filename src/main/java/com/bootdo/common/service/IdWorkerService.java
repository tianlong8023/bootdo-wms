package com.bootdo.common.service;

/**
 * 獲取18位時序id
 */
public interface IdWorkerService {

    public static final String IMPL = "idWorkerService";

    Long nextLongId();

    String nextStringId();
}

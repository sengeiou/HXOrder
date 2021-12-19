package com.happysnaker.service.observer;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/18
 * @email happysnaker@foxmail.com
 */
public interface Observer {
    /**
     * 观察者方法
     * @param obj
     * @param args
     */
    void acton(Object obj, Object... args);
}

package com.yichen.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 通用Bean转换工具类
 */
@Component
public class BeanConverter {

    /**
     * 将源对象转换为目标类型对象
     *
     * @param source 源对象
     * @param targetClass 目标类型
     * @param <T> 目标类型泛型
     * @return 目标类型对象
     */
    public <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Bean转换失败", e);
        }
    }

    /**
     * 将源对象转换为目标类型对象，使用提供的目标对象实例
     *
     * @param source 源对象
     * @param targetSupplier 目标对象提供者
     * @param <T> 目标类型泛型
     * @return 目标类型对象
     */
    public <T> T convert(Object source, Supplier<T> targetSupplier) {
        if (source == null) {
            return null;
        }
        
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 将源对象列表转换为目标类型对象列表
     *
     * @param sourceList 源对象列表
     * @param targetClass 目标类型
     * @param <S> 源类型泛型
     * @param <T> 目标类型泛型
     * @return 目标类型对象列表
     */
    public <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }
        
        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S source : sourceList) {
            targetList.add(convert(source, targetClass));
        }
        return targetList;
    }
} 
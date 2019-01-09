/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sqlobjecttree;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.common.sqljep.function.Comparative;

/**
 * �����ɸѡ��
 * 
 * @author shenxun
 *
 */
public interface ComparativeMapChoicer {

    /**
     * ����PartinationSet ��ȡ����������Ӧֵ��map.
     * @param arguments
     * @param partnationSet
     * @return
     */
    Map<String, Comparative> getColumnsMap(List<Object> arguments, Set<String> partnationSet);
}

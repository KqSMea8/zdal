package com.alipay.zdal.datasource.resource.adapter.jdbc.vender;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * com.alipay.zdatasource.resource.adapter.jdbc.vendor package ��������Ĳ����׼�
 * 
 * @author liangjie.li
 * @version $Id: RunAllSorterTestsSuite.java, v 0.1 2012-8-15 ����3:06:49 liangjie.li Exp $
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MySQLExceptionSorterTest.class, OracleExceptionSorterTest.class })
public class RunAllSorterTestsSuite {
}

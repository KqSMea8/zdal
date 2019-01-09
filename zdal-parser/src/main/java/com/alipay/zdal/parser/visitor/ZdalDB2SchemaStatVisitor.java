/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.parser.visitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ������Oracle��sql�����ں�,ֻ�Ƕ��ڷ�ҳ���߼�,����������Ĵ���,��֧��DB2�ķ�ҳ�﷨����.
 * @author 伯牙
 * @version $Id: ZdalDB2SchemaStatVisitor.java, v 0.1 2013-12-31 ����09:42:39 Exp $
 */
public class ZdalDB2SchemaStatVisitor extends ZdalOracleSchemaStatVisitor {

    /** 
     * 
     * @see com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor#getRownums()
     */
    public Set<BindVarCondition> getRownums() {
        if (limitColumnName == null) {
            return new HashSet<BindVarCondition>();
        } else {
            Set<BindVarCondition> results = new HashSet<BindVarCondition>();
            List<BindVarCondition> bindConditions = super.getBindVarConditions();//�Ӱ󶨲����л�ȡ.
            for (BindVarCondition bindVarCondition : bindConditions) {
                if (bindVarCondition.getColumnName().equalsIgnoreCase(limitColumnName)) {
                    BindVarCondition tmp = new BindVarCondition();
                    if (bindVarCondition.getOperator().equals(">")
                        || bindVarCondition.getOperator().equals(">=")) {
                        tmp.setColumnName(OFFSET);
                        tmp.setIndex(bindVarCondition.getIndex());
                        tmp.setOperator(bindVarCondition.getOperator());
                        results.add(tmp);
                    } else if (bindVarCondition.getOperator().equals("<")
                               || bindVarCondition.getOperator().equals("<=")) {
                        tmp.setColumnName(ROWCOUNT);
                        tmp.setIndex(bindVarCondition.getIndex());
                        tmp.setOperator(bindVarCondition.getOperator());
                        results.add(tmp);
                    }
                }
            }
            return results;

        }
    }
}

package com.alipay.zdal.test.client;

import org.junit.Test;
import static com.alipay.ats.internal.domain.ATS.Step;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;

@RunWith(ATSJUnitRunner.class)
@Feature("��zdc���������ļ�")
public class SR951090 {
	
	@Subject("��zdc�ϻ�ȡ�����ļ�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951091(){
		Step("��zdc�ϻ�ȡ�����ļ�");
	//	ZdalDataSource zd = (ZdalDataSource) ZdalTestSuite.context
	//	.getBean("zdalClientGetFileFromZdc");
		
	}

}

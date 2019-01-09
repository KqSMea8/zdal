package com.alipay.zdal.test.shardrw;

import org.junit.Test;
import static com.alipay.ats.internal.domain.ATS.Step;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.util.dispatchanalyzer.ZdalDatasourceIntrospector;

@RunWith(ATSJUnitRunner.class)
@Feature("ȫ�����:isDataBaseAvailable")
public class SR954060 {
	public TestAssertion Assert = new TestAssertion();
	ZdalDatasourceIntrospector td =null;
	
	@Subject("������������кţ��жϸ�db�Ƿ����")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954061(){
		Step("������������кţ��жϸ�db�Ƿ����");
		String[] springXmlPath = {  "./shardrw/spring-available-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springXmlPath);
		
		td = (ZdalDatasourceIntrospector) context
		.getBean("isDataBaseAvailableTest");
		boolean b1=td.isDataBaseAvailable(0, 1);		
		boolean b2=td.isDataBaseAvailable(1, 1);
		Assert.areEqual(true, b1==false&&b2==true, "isDataBaseAvailable��֤,b1="+b1+",b2="+b2);
	
		
	}
}

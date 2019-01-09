package com.alipay.zdal.test.shardrw;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static com.alipay.ats.internal.domain.ATS.Step;
import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.util.dispatchanalyzer.ZdalDatasourceIntrospector;

@RunWith(ATSJUnitRunner.class)
@Feature("ȫ�����:getAvailableDBIndexes ,getNotAvailableDBIndexes")
public class SR954030 {
	public TestAssertion Assert = new TestAssertion();
	ZdalDatasourceIntrospector td =null;
	
	@Subject("��ȡָ��group�еĿ���db")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954032(){
			Step("��ȡָ��group�еĿ���db");
		String[] springXmlPath = {  "./shardrw/spring-available-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springXmlPath);

		td = (ZdalDatasourceIntrospector) context
		.getBean("getAvailableDBIndexs");
		List<Integer> list =td.getAvailableDBIndexes(0);
		List<Integer> list1 =td.getAvailableDBIndexes(1);
		List<Integer> list2=td.getNotAvailableDBIndexes(0);
		List<Integer> list3=td.getNotAvailableDBIndexes(1);
		Assert.areEqual(true, list.size()==0&&list1.size()==1&&list2.size()==2&&list3.size()==1, "��ȡgetAvailableDBIndexes,getNotAvailableDBIndexes");
		Assert.areEqual(true, list1.get(0)==0&&list3.get(0)==1, "getAvailableDBIndexes,getNotAvailableDBIndexes");	
	
	}

}

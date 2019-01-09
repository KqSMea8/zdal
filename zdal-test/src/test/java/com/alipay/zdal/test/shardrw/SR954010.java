package com.alipay.zdal.test.shardrw;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
import org.junit.Test;
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
@Feature("ȫ�����:getAllTableNames")
public class SR954010 {
	public TestAssertion Assert = new TestAssertion();
	ZdalDatasourceIntrospector td;
	
	@Subject("�����߼���������ȡ�����������")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954011(){
		Step("�����߼���������ȡ�����������");
		String[] springXmlPath = {  "./shardrw/spring-available-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springXmlPath);
		
		td = (ZdalDatasourceIntrospector) context
		.getBean("getAllTableNames");
		Map<String, List<String>> mp=td.getAllTableNames("users");
		
		Assert.areEqual(4, mp.size(), "��֤��ĸ���");
		Iterator iter = mp.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String db = (String) entry.getKey();
			List<String> tb = (List) entry.getValue();
            Assert.areEqual(5, tb.size(), "��֤��ĸ���");			
		}		
	}

}

package com.alipay.zdal.test.shardrw;

import java.util.HashMap;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.util.Map;

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
@Feature("ȫ�����:getAvailableGroupDB")
public class SR954040 {
	public TestAssertion Assert = new TestAssertion();
	ZdalDatasourceIntrospector td =null;

	@Subject("��ȡuser_id��ֵȷ��groupnum�����ظ�group�п��õ�db���к�")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954041(){
		Step("��ȡuser_id��ֵȷ��groupnum�����ظ�group�п��õ�db���к�");
		String[] springXmlPath = {  "./shardrw/spring-available-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springXmlPath);
		
		td = (ZdalDatasourceIntrospector) context
		.getBean("getAvailableGroupDB");
		Map<String,String> map = new HashMap<String, String>();
		map.put("user_id", "5");
		String str=td.getAvailableGroupDB(map);
		Assert.areEqual("1", str, "getAvailableGroupDB��֤:"+str);
		
		
		
	}
}

package com.alipay.zdal.test.shardrw;

import org.junit.Before;
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
@Feature("ȫ�����: getAvailableDBAndTableByWeights")
public class SR954020 {
	public TestAssertion Assert = new TestAssertion();
	ZdalDatasourceIntrospector td;

	@Before
	public void beforeTestCase() {
		String[] springXmlPath = {  "./shardrw/spring-available-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springXmlPath);
		
		td = (ZdalDatasourceIntrospector) context
				.getBean("getAvailableDBAndTableByWeights");
	}

	@Subject("��ȡָ��group�е��߼�������Ӧ�Ŀ��������")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954021() {
		Step("��ȡָ��group�е��߼�������Ӧ�Ŀ��������");
		String[] re = td.getAvailableDBAndTableByWeights("users", 0, true);
		Assert.areEqual(true,
				Integer.parseInt(re[0]) == 0 || Integer.parseInt(re[0]) == 1,
				"��ȡ���ñ���");
		Assert.areEqual(
				true,
				re[1].equalsIgnoreCase("users_0")
						|| re[1].equalsIgnoreCase("users_1")
						|| re[1].equalsIgnoreCase("users_2")
						|| re[1].equalsIgnoreCase("users_3")
						|| re[1].equalsIgnoreCase("users_4"), "��ȡ���ñ���");
	}

	@Subject("ָ����groupNumԽ��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954022() {
		try{
			Step("ָ����groupNumԽ��");
		 td.getAvailableDBAndTableByWeights("users", 99, true);
		}catch(Exception ex){
			ex.printStackTrace();
			Assert.areEqual(IllegalArgumentException.class, ex.getClass(), "�����쳣");
		}
	}
		
	@Subject("ָ�����߼�����������")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC954023() {
		try{
			Step("ָ�����߼�����������");
		 td.getAvailableDBAndTableByWeights("userstest", 0, true);
		}catch(Exception ex){
			ex.printStackTrace();
			Assert.areEqual(IllegalArgumentException.class, ex.getClass(), "�����쳣");
		}
	}
	

}

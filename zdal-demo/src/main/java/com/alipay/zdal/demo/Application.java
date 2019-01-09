package com.alipay.zdal.demo;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.client.util.condition.DBSelectorIDRouteCondition;
import com.alipay.zdal.demo.biz.CityService;
import com.alipay.zdal.demo.model.City;
import com.alipay.zdal.demo.util.ZdalRuleParser;

public class Application {

	private CityService service;
	private ZdalDataSource dataSource;
	// 所有配置的物理数据源, dbIndex
	private Map<Integer, String> logicPhysicsIndexes;

	// 库名
	private String dbName = "zdal_db";
	// 表名
	private String tbName = "t_city";

	
	public static void main(String[] args) {
		Application application = new Application();
		application.init();
		application.insert();
	}
	
	public void init() {
		// 加载spring容器，实例化bean
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		service = (CityService) context.getBean("cityService");
		dataSource = (ZdalDataSource) context.getBean("dataSource");

		// 实例化逻辑数据源
		logicPhysicsIndexes = new HashMap<Integer, String>();
		// 得到物理数据源名称
		Map<String, String> logicPhysicsDsNames = dataSource.getZdalConfig().getLogicPhysicsDsNames();
		// 将其存入物理数据源数据 {(0,master_00),(1,master_01)...}
		for (String name : logicPhysicsDsNames.keySet()) {
			String[] split = name.split("_");
			logicPhysicsIndexes.put(Integer.valueOf(split[1]), name);
		}
	}

	/**
	 * 插入数据库操作
	 */
	public void insert() {

		// 设置插入10条id数据，id自增
		for (int id = 30; id < 40; id++) {

			// 指定查詢库的路由規則
			// 根据规则得到分库索引
			int dbIndex = ZdalRuleParser.parserDbIndex(id);
			String dbSelectorID = logicPhysicsIndexes.get(dbIndex);
			System.out.println("dbSelectorID = " + dbSelectorID);
			// 根据规则得到分表索引
			int tbIndex = ZdalRuleParser.parserTbIndex(id);
			String tablePostfix = new DecimalFormat("_00").format(tbIndex);// 如果tbIndex为3，tablePostfix为_03
			// 根据分表索引得到物理表名称
			String physicTableName = tbName + tablePostfix;
			System.out.println("physicTableName = " + physicTableName);// physicTableName = t_city_00、t_city_01、t_city_02等
			// 得到 数据库选择器标识路由条件---确定是存在哪一个数据库中
			DBSelectorIDRouteCondition dbSelectorIDRouteCondition = new DBSelectorIDRouteCondition("t_city", dbSelectorID, physicTableName);
			// 将分库分表添加到线程中
			ThreadLocalMap.put(ThreadLocalString.ROUTE_CONDITION, dbSelectorIDRouteCondition);

			System.out.println("配置完成，准备开始插入数据");
			City city = new City(id, "北京");
			service.insert(city);
		}
	}
}
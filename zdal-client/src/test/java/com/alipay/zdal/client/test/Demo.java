package com.alipay.zdal.client.test;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Demo {

	public static void main(String[] args) {
		String[] fileNames = { "//Users/pengwei/github/zdal/zdal-client/configs/Shard/Shard-dev-ds.xml",
				"//Users/pengwei/github/zdal/zdal-client/configs/Shard/Shard-dev-rule.xml" };
		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("./configs/Shard/Shard-dev-rule.xml");
		Object bean = ctx.getBean("testRWRule");
		System.out.println(bean);
	}
}

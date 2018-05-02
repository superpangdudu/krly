package cn.krly.service.payment.order;

import cn.krly.platform.api.payment.pojo.Order;
import cn.krly.service.payment.order.mapper.OrderMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =
				SpringApplication.run(OrderServiceApplication.class, args);

		OrderMapper mapper = ctx.getBean(OrderMapper.class);
		Order order = mapper.selectByPrimaryKey(1);
	}
}

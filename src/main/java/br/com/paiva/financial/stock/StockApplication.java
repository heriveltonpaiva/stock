package br.com.paiva.financial.stock;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class StockApplication implements CommandLineRunner {

	@Autowired
	private OperationRepository repository;

	public static void main(String[] args) {

		SpringApplication.run(StockApplication.class, args);

	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println("Iniciando...");

		/**
		final Operation op1 = new Operation();
		op1.setStockName("COGN3");
		op1.setQuantity(300);
		op1.setOperationPrice(1446.0);
		op1.setType(OperationType.BUY);
		op1.setAveragePrice(0D);

		repository.save(op1);
  **/
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Operation operation : repository.findAll()) {
			System.out.println(operation);
		}

	}

}

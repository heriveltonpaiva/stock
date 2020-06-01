package br.com.paiva.financial.stock.trade.operation;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OperationRepository extends MongoRepository<Operation, String> {

    List<Operation> findByStockName(final String stockName);

}

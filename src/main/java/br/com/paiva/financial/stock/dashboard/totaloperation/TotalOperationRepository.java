package br.com.paiva.financial.stock.dashboard.totaloperation;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TotalOperationRepository extends MongoRepository<TotalOperation, String> {

    TotalOperation findByReferenceMonthAndStockName(final Integer referenceMonth, final String stockName);

}

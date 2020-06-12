package br.com.paiva.financial.stock.dashboard.totaloperation.month;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TotalOperationMonthRepository
    extends MongoRepository<TotalOperationMonth, String> {

  TotalOperationMonth findByReferenceMonth(final Integer referenceMonth);
}

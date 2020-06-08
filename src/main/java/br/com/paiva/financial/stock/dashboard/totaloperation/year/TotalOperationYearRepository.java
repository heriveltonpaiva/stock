package br.com.paiva.financial.stock.dashboard.totaloperation.year;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TotalOperationYearRepository extends MongoRepository<TotalOperationYear, String> {

  TotalOperationYear findByReferenceYear(final Integer referenceYear);
}

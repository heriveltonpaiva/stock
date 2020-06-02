package br.com.paiva.financial.stock.trade.tax;

import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static br.com.paiva.financial.stock.util.StockUtils.getToY;
import static br.com.paiva.financial.stock.util.StockUtils.round;

@Component
@RequiredArgsConstructor
public class TaxService {

  private Tax dtoToEntity(final TaxDTO dto) {
    Tax tax = new Tax();
    tax.setBrokerage(dto.getBrokerage());
    tax.setEmoluments(dto.getEmoluments());
    tax.setLiquidation(dto.getLiquidation());
    tax.setOtherTaxes(dto.getOtherTaxes());
    tax.setTaxes(dto.getTaxes());
    tax.setIncomingTax(dto.getIncomingTax());
    return tax;
  }

  public Tax createTradingNoteTax(final TaxDTO dto){
    return dtoToEntity(dto);
  }

  public Tax createOperationTax(final TradingNote dto, final Double operationValue, final OperationType type){
     Tax newTax = new Tax();
    newTax.setEmoluments(round(getToY(dto.getValue(), dto.getTaxes().getEmoluments(), operationValue)));
    newTax.setLiquidation(round(getToY(dto.getValue(), dto.getTaxes().getLiquidation(), operationValue)));
    newTax.setTaxes(round(getToY(dto.getValue(), dto.getTaxes().getTaxes(), operationValue)));
    newTax.setIncomingTax(type == OperationType.SELL ? round(getToY(dto.getValueSell(), dto.getTaxes().getIncomingTax(), operationValue)) : 0D);
    newTax.setOtherTaxes(round(getToY(dto.getValue(), dto.getTaxes().getOtherTaxes(), operationValue)));

    return newTax;
  }



}

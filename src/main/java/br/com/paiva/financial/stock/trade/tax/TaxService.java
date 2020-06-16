package br.com.paiva.financial.stock.trade.tax;

import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.trade.tradingnote.BrokerType;
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

  public Tax calculateTaxes(final TradingNote note, final Double operationValue, final OperationType type){
    Tax tax = new Tax();
    tax.setEmoluments(round(getToY(note.getValue(), note.getTaxes().getEmoluments(), operationValue)));
    tax.setLiquidation(round(getToY(note.getValue(), note.getTaxes().getLiquidation(), operationValue)));
    tax.setTaxes(round(getToY(note.getValue(), note.getTaxes().getTaxes(), operationValue)));
    tax.setIncomingTax(type == OperationType.SELL ? round(getToY(note.getValueSell(), note.getTaxes().getIncomingTax(), operationValue)) : 0D);
    tax.setOtherTaxes(round(getToY(note.getValue(), note.getTaxes().getOtherTaxes(), operationValue)));
    tax.setBrokerage(note.getBroker().equals(BrokerType.XP)? 18.9 : 0D);
    return tax;
  }

}

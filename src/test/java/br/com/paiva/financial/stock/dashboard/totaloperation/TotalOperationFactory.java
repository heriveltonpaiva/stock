package br.com.paiva.financial.stock.dashboard.totaloperation;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.trade.tax.Tax;

import java.time.LocalDate;

public class TotalOperationFactory {

    static TotalOperation totalOperationBUY(final Integer quantity){
        TotalOperation totalOperation = new TotalOperation();

        totalOperation.setBuyQuantity(quantity);
        totalOperation.setTotalPurchased(quantity * 10D);
        totalOperation.setReferenceMonth(LocalDate.now().getMonth().getValue());
        totalOperation.setStockName("RENT3");
        totalOperation.setTotalEmoluments(quantity * 1D);
        totalOperation.setTotalLiquidation(quantity * 1D);
        totalOperation.setTotalIncomingTax(0D);
        totalOperation.setTotalGainValue(0D);
        totalOperation.setTotalDarf(0D);
        totalOperation.setTotalOtherTaxes(quantity * 1D);
        totalOperation.setTotalTaxes(quantity * 1D);
        totalOperation.setTotalOperationalCosts(quantity * 3D);
        totalOperation.setLastModified(LocalDate.now());

        return totalOperation;
    }

    static TotalOperation totalOperationSELL(final Integer quantity){
        Double tax = quantity * 1D;
        TotalOperation totalOperation = new TotalOperation();

        totalOperation.setTotalPurchased(quantity * 10D);
        totalOperation.setSellQuantity(quantity);
        totalOperation.setTotalSold(quantity * 20D);
        totalOperation.setReferenceMonth(LocalDate.now().getMonth().getValue());
        totalOperation.setStockName("RENT3");

        totalOperation.setTotalEmoluments(tax);
        totalOperation.setTotalLiquidation(tax);
        totalOperation.setTotalIncomingTax(tax);
        totalOperation.setTotalOtherTaxes(tax);
        totalOperation.setTotalTaxes(tax);
        totalOperation.setTotalOperationalCosts(tax * 3D);
        totalOperation.setTotalGainValue(quantity * 20D - (quantity * 3D) - totalOperation.getTotalPurchased() - totalOperation.getTotalOperationalCosts());
        totalOperation.setTotalDarf(totalOperation.getTotalGainValue() * 0.15);
        totalOperation.setLastModified(LocalDate.now());

        return totalOperation;
    }

    public static Tax taxBUY(final Integer quantity) {
        final Tax tax = Tax.builder()
                .liquidation(quantity * 1D)
                .brokerage(quantity * 1D)
                .emoluments(quantity * 1D)
                .taxes(quantity * 1D)
                .incomingTax(0D)
                .otherTaxes(quantity * 1D)
                .totalValue(quantity * 5D)
                .operationalCosts(quantity * 3D)
                .build();
        return tax;
    }

    public static Tax taxSELL(final Integer quantity) {
        final Tax tax = Tax.builder()
                .liquidation(quantity * 1D)
                .brokerage(quantity * 1D)
                .emoluments(quantity * 1D)
                .taxes(quantity * 1D)
                .incomingTax(quantity * 1D)
                .otherTaxes(quantity * 1D)
                .totalValue(quantity * 6D)
                .operationalCosts(quantity * 3D)
                .build();
        return tax;
    }

    public static Operation operationBUY(final Integer quantity) {
        final Operation op2 = new Operation();
        op2.setStockName("RENT3");
        op2.setQuantity(quantity);
        op2.setOperationPrice(quantity * 10D);
        op2.setPurchasePrice(quantity * 10D);
        op2.setType(OperationType.BUY);
        op2.setTaxes(taxBUY(quantity));

        return op2;
    }

    public static Operation operationSELL(final Integer quantity) {
        final Operation op1 = new Operation();
        op1.setStockName("RENT3");
        op1.setQuantity(quantity);
        op1.setOperationPrice(quantity * 20D);
        op1.setPurchasePrice(quantity * 10D);
        op1.setType(OperationType.SELL);
        op1.setTaxes(taxSELL(quantity));
        return op1;
    }

}

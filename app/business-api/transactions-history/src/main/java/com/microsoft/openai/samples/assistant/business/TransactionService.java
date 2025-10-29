package com.microsoft.openai.samples.assistant.business;

import com.microsoft.openai.samples.assistant.business.Transaction;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private Map<String,List<Transaction>> lastTransactions= new HashMap<>();
    private Map<String,List<Transaction>> allTransactions= new HashMap<>();

     public TransactionService(){

         lastTransactions.put("1000", new ArrayList<>(Arrays.asList(
                new Transaction("201", "Pago mensual oficina", "outcome", "Servicios Hidráulicos Rivera", "CLABE2001", "1000", "Transferencia bancaria", "8500.00", "2024-03-15T09:30:00Z"),
                new Transaction("202", "Suscripción plataforma financiera", "outcome", "Finanzas Patria", "CLABE2002", "1000", "Tarjeta de crédito", "4200.00", "2024-02-10T10:15:00Z"),
                new Transaction("203", "Pago seguro empresarial", "outcome", "Seguros Continental", "CLABE2003", "1000", "Débito automático", "6100.00", "2023-12-01T11:20:00Z")
         )));

         lastTransactions.put("1010",new ArrayList<> (Arrays.asList(
                new Transaction("11", "Pago de la factura 334398", "outcome","Servicios Agua Viva", "CLABE0001", "1010", "Transferencia bancaria", "100000.00", "2024-04-01T12:00:00Z"),
                new Transaction("22", "Pago de la factura 4613","outcome", "EnerLatam", "CLABE0002", "1010", "Tarjeta de crédito", "200000.00", "2024-03-02T12:00:00Z"),
                new Transaction("33", "Pago de la nómina especial","outcome", "Talento Andino", "CLABE0003", "1010", "Transferencia bancaria", "300000.00", "2023-10-03T12:00:00Z"),
                new Transaction("43", "Abono a crédito empresarial","outcome", "Finanzas Patria", "CLABE0004", "1010", "Débito automático", "400000.00", "2023-08-04T12:00:00Z"),
                new Transaction("53", "Pago de proveedores trimestral","outcome", "Distribuciones Pacífico", "CLABE0005", "1010", "Transferencia bancaria", "500000.00", "2023-04-05T12:00:00Z"))
         ));

         lastTransactions.put("1020", new ArrayList<>(Arrays.asList(
                new Transaction("301", "Pago campaña publicitaria", "outcome", "Marketing Andino", "CLABE3001", "1020", "Transferencia bancaria", "950000.00", "2024-04-05T08:45:00Z"),
                new Transaction("302", "Renovación licencia software", "outcome", "Tecnología Austral", "CLABE3002", "1020", "Tarjeta de crédito", "425000.00", "2024-01-18T15:00:00Z"),
                new Transaction("303", "Pago arriendo sucursal", "outcome", "Inmobiliaria Pacífico", "CLABE3003", "1020", "Transferencia bancaria", "780000.00", "2023-11-28T13:10:00Z")
         )));


         allTransactions.put("1000", new ArrayList<>(Arrays.asList(
                new Transaction("201", "Pago mensual oficina", "outcome", "Servicios Hidráulicos Rivera", "CLABE2001", "1000", "Transferencia bancaria", "8500.00", "2024-03-15T09:30:00Z"),
                new Transaction("204", "Pago de mantenimiento", "outcome", "Servicios Hidráulicos Rivera", "CLABE2004", "1000", "Transferencia bancaria", "9200.00", "2023-09-15T09:30:00Z"),
                new Transaction("205", "Gasto de representación", "outcome", "Eventos del Norte", "CLABE2005", "1000", "Tarjeta de crédito", "5100.00", "2023-05-19T12:10:00Z"),
                new Transaction("202", "Suscripción plataforma financiera", "outcome", "Finanzas Patria", "CLABE2002", "1000", "Tarjeta de crédito", "4200.00", "2024-02-10T10:15:00Z"),
                new Transaction("206", "Pago póliza empresarial", "outcome", "Seguros Continental", "CLABE2006", "1000", "Débito automático", "6100.00", "2022-12-01T11:20:00Z"),
                new Transaction("203", "Pago seguro empresarial", "outcome", "Seguros Continental", "CLABE2003", "1000", "Débito automático", "6100.00", "2023-12-01T11:20:00Z")
         )));

         allTransactions.put("1010",new ArrayList<>(Arrays.asList(
                new Transaction("11", "Pago programado con referencia 0001","outcome", "Servicios Agua Viva", "CLABE0001", "1010", "Transferencia bancaria", "100000.00", "2024-04-01T12:00:00Z"),
                new Transaction("21", "Pago de mantenimiento 4200","outcome", "Servicios Agua Viva", "CLABE0002", "1010", "Transferencia bancaria", "200000.00", "2024-01-02T12:00:00Z"),
                new Transaction("31", "Pago de la factura 3743","outcome", "Servicios Agua Viva", "CLABE0003", "1010", "Débito automático", "300000.00", "2023-10-03T12:00:00Z"),
                new Transaction("41", "Pago del leasing 8921","outcome", "Servicios Agua Viva", "CLABE0004", "1010", "Transferencia", "400000.00", "2023-08-04T12:00:00Z"),
                new Transaction("51", "Pago de la factura 7666","outcome", "Servicios Agua Viva", "CLABE0005", "1010", "Tarjeta de crédito", "500000.00", "2023-04-05T12:00:00Z"),

                new Transaction("12", "Pago de la factura 5517","outcome", "EnerLatam", "CLABE1001", "1010", "Tarjeta de crédito", "100000.00", "2024-03-01T12:00:00Z"),
                new Transaction("22", "Pago de la factura 682222","outcome", "EnerLatam", "CLABE1002", "1010", "Tarjeta de crédito", "200000.00", "2023-01-02T12:00:00Z"),
                new Transaction("32", "Pago de servicios 94112","outcome", "EnerLatam", "CLABE1003", "1010", "Transferencia", "300000.00", "2022-10-03T12:00:00Z"),
                new Transaction("42", "Pago de póliza 23122","outcome", "EnerLatam", "CLABE1004", "1010", "Transferencia", "400000.00", "2022-08-04T12:00:00Z"),
                new Transaction("52", "Pago de servicios 171443","outcome", "EnerLatam", "CLABE1005", "1010", "Transferencia", "500000.00", "2020-04-05T12:00:00Z")
         )));

         allTransactions.put("1020", new ArrayList<>(Arrays.asList(
                new Transaction("301", "Pago campaña publicitaria", "outcome", "Marketing Andino", "CLABE3001", "1020", "Transferencia bancaria", "950000.00", "2024-04-05T08:45:00Z"),
                new Transaction("304", "Pago de logística", "outcome", "Distribuciones Pacífico", "CLABE3004", "1020", "Transferencia bancaria", "540000.00", "2023-09-12T10:05:00Z"),
                new Transaction("305", "Pago de consultoría", "outcome", "Consultores Altiplano", "CLABE3005", "1020", "Tarjeta de crédito", "365000.00", "2023-07-25T16:40:00Z"),
                new Transaction("302", "Renovación licencia software", "outcome", "Tecnología Austral", "CLABE3002", "1020", "Tarjeta de crédito", "425000.00", "2024-01-18T15:00:00Z"),
                new Transaction("306", "Pago de póliza", "outcome", "Seguros Continental", "CLABE3006", "1020", "Débito automático", "295000.00", "2022-12-02T09:00:00Z"),
                new Transaction("303", "Pago arriendo sucursal", "outcome", "Inmobiliaria Pacífico", "CLABE3003", "1020", "Transferencia bancaria", "780000.00", "2023-11-28T13:10:00Z")
         )));



     }
    public List<Transaction> getTransactionsByRecipientName(String accountId, String name) {

        if (accountId == null || accountId.isEmpty())
            throw new IllegalArgumentException("El identificador de cuenta está vacío o es nulo");
        try {
            Integer.parseInt(accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El identificador de cuenta no es un número válido");
        }

    if ( allTransactions.get(accountId) == null) return new ArrayList<>();
        else
      return  allTransactions.get(accountId).stream()
                .filter(transaction -> transaction.recipientName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

    }

    public List<Transaction> getlastTransactions(String accountId) {
        if (accountId == null || accountId.isEmpty())
            throw new IllegalArgumentException("El identificador de cuenta está vacío o es nulo");
        try {
            Integer.parseInt(accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El identificador de cuenta no es un número válido");
        }

        if ( lastTransactions.get(accountId) == null) return new ArrayList<>();
        else
        return lastTransactions.get(accountId);
    }

    public void notifyTransaction(String accountId,Transaction transaction){
        if (accountId == null || accountId.isEmpty())
            throw new IllegalArgumentException("El identificador de cuenta está vacío o es nulo");
        try {
            Integer.parseInt(accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El identificador de cuenta no es un número válido");
        }

         var transactionsList = allTransactions.get(accountId);
         if ( transactionsList == null)
             throw new RuntimeException("No se encontraron transacciones históricas para la cuenta: "+accountId);
        transactionsList.add(transaction);

        var lastTransactionsList = lastTransactions.get(accountId);
        if ( lastTransactionsList == null)
            throw new RuntimeException("No se encontraron transacciones recientes para la cuenta: "+accountId);
        lastTransactionsList.add(transaction);


    }
}

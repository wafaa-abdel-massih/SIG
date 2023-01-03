package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOperations {

    ArrayList<InvoiceHeader> invoiceHeader;
    InvoiceHeader invoice;
    Scanner invoiceHeaderData, invoiceLineData;
    String[] dataRow;

    InvoiceLine invoiceLine;
    ArrayList<InvoiceLine> invoiceLines;
    int invoiceNO;
    double total;

    public int lineSize=0;

    public ArrayList<InvoiceHeader> readFile(String headerPath, String linePath){

        invoiceHeader = new ArrayList<>();
        dataRow = new String[4];

        try {
            //"./src/main/resources/InvoiceHeader.csv"
            //"./src/main/resources/InvoiceLine.csv"
            invoiceHeaderData = new Scanner(new File(headerPath));
            invoiceLineData = new Scanner(new File(linePath));

            while (invoiceHeaderData.hasNext())
            {
                invoice = new InvoiceHeader();
                dataRow = invoiceHeaderData.next().split(",");

                invoice.setInvoiceNum(Integer.parseInt(dataRow[0]));
                invoice.setInvoiceDate(dataRow[1]);
                invoice.setCustomerName(dataRow[2]);

                invoiceHeader.add(invoice);

            }
            invoiceNO = 1;
            invoiceLines = new ArrayList<>();
            total =0;
            while (invoiceLineData.hasNext()){

                dataRow = invoiceLineData.next().split(",");
                if (Integer.parseInt(dataRow[0])==invoiceNO){
                    this.lineSize++;
                    invoiceLine = new InvoiceLine();
                }
                else {
                    this.lineSize++;
                    invoiceHeader.get(invoiceNO-1).setTotal(total);
                    total=0;
                    invoiceHeader.get(invoiceNO-1).setInvoiceLines(invoiceLines);
                    invoiceNO = Integer.parseInt(dataRow[0]);
                    invoiceLine = new InvoiceLine();
                    invoiceLines = new ArrayList<>();
                }

                total += Double.parseDouble(dataRow[2]) * Double.parseDouble(dataRow[3]);
                invoiceLine.setItemTotal(Double.parseDouble(dataRow[2]) * Double.parseDouble(dataRow[3]));
                invoiceLine.setItemName(dataRow[1]);
                invoiceLine.setItemPrice(Double.parseDouble(dataRow[2]));
                invoiceLine.setCount(Integer.parseInt(dataRow[3]));
                invoiceLines.add(invoiceLine);
            }

            invoiceHeader.get(invoiceNO-1).setTotal(total);
            invoiceHeader.get(invoiceNO-1).setInvoiceLines(invoiceLines);
            invoiceHeaderData.close();
            invoiceLineData.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return invoiceHeader;
    }

    public void writeFile(ArrayList<InvoiceHeader> invoiceHeader){

        File invoiceHeaderFile = new File("InvoiceHeader.csv");
        File invoiceLineFile = new File("InvoiceLine.csv");
        FileWriter headerWriter, lineWriter;

        try {
            headerWriter = new FileWriter(invoiceHeaderFile);
            lineWriter = new FileWriter(invoiceLineFile);
            for (InvoiceHeader header : invoiceHeader){

                String invoice = header.getInvoiceNum() + "," +
                        header.getInvoiceDate() + "," +
                        header.getCustomerName() + "\n";

                headerWriter.write(invoice);

                for (InvoiceLine line : header.getInvoiceLines()){

                    String invoiceLine = header.getInvoiceNum() + "," +
                            line.getItemName() + "," +
                            line.getItemPrice() + "," +
                            line.getCount() + "\n";

                    lineWriter.write(invoiceLine);
                }
            }
            headerWriter.close();
            lineWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

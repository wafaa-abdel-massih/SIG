package Model;

import java.util.ArrayList;

public class ModelTestMain {

    public static void main(String[] args) {

        FileOperations testReadingObj = new FileOperations();
        ArrayList<InvoiceHeader> testInvoiceHeader;
        testInvoiceHeader = testReadingObj.readFile();

        // test method for reading to test reading from the provided CSV files
        printTestReadingData(testInvoiceHeader);
    }

    private static void printTestReadingData(ArrayList<InvoiceHeader> testArray){

        for (InvoiceHeader test : testArray){
            System.out.println(test.getInvoiceNum());
            System.out.println("{");
            System.out.println(test.getInvoiceDate()+", "+test.getCustomerName());
            for (InvoiceLine testLine : test.getInvoiceLines()){
                System.out.println(testLine.getItemName()+", "+testLine.getItemPrice()+", "+testLine.getCount());
            }
            System.out.println("}");
        }
    }
}

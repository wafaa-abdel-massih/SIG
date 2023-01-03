package Model;

import Controller.ControllerClass;

import java.util.ArrayList;

public class ModelTestMain {

    public static void main(String[] args) {

        FileOperations testReadingObj = new FileOperations();
        ArrayList<InvoiceHeader> testInvoiceHeader;
        testInvoiceHeader = testReadingObj.readFile("./src/main/resources/InvoiceHeader.csv",
                "./src/main/resources/InvoiceLine.csv");

        // test method for reading to test reading from the provided CSV files
        printTestReadingData(testInvoiceHeader);

        ControllerClass c = new ControllerClass();
        c.load();
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

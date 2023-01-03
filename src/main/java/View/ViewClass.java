package View;
import Controller.ControllerClass;
import Model.FileOperations;
import Model.InvoiceHeader;
import Model.InvoiceLine;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewClass extends JFrame{

    protected JMenuItem loadFileItem, saveFileItem;

    public JTable invoicesTable, invoiceItemsTable;
    public DefaultTableModel invoiceModel, lineModel;
    protected JButton createNewInvoice, deleteInvoice, createItem, deleteItem,
            okHeader, cancelHeader, okLine, cancelLine;

    public JLabel invoiceNumLabel = new JLabel(" "),
            invoicesTotal = new JLabel(" ");
    public JTextField invoiceDateDialog, customerNameDialog, itemName,
            itemPrice, itemCount, invoiceDateTF, customerNameTF;
    private final String[] item = {"No.","Item Name","Item Price","Count","Item Total"};
    private final String[] invoice = {"No.","Date","Customer","Total"};
    String[][] lineData = {};
    JDialog headerDialog, lineDialog;
    private  ControllerClass control = new ControllerClass(this);
    InvoiceHeader row;
    public InvoiceHeader header;
    public InvoiceLine line;
    FileOperations file = new FileOperations();

    public ViewClass(){};

    public ViewClass(String[][] headerData){
        super("Sales Invoice Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,650);
        setResizable(false);

        createMenuBar();

        setLayout(new GridLayout(1,2));

        createLeftSidePanel(headerData);
        createRightSidePanel();

        setVisible(true);
        addEventListeners();
    }

    private void createMenuBar(){

        JMenuBar fileMenuBar = new JMenuBar();
        loadFileItem = new JMenuItem("Load File");
        loadFileItem.addActionListener(control);
        saveFileItem = new JMenuItem("Save File");
        saveFileItem.addActionListener(control);
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(loadFileItem);
        fileMenu.addSeparator();
        fileMenu.add(saveFileItem);
        fileMenuBar.add(fileMenu);
        setJMenuBar(fileMenuBar);

    }

    private void createLeftSidePanel(String[][] header){

        JPanel leftSidePanel = new JPanel();
        add(leftSidePanel);

        leftSidePanel.add(new JLabel("Invoice Table"));

        invoiceModel = new DefaultTableModel(header, invoice);
        invoicesTable = new JTable(invoiceModel);
        invoicesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                row = control.headers.get(invoicesTable.getSelectedRow());
                int selectedIndex = invoicesTable.getSelectedRow();
                if (selectedIndex != -1) {
                    invoiceNumLabel.setText(" " + row.getInvoiceNum());
                    invoiceDateTF.setText(row.getInvoiceDate());
                    customerNameTF.setText(row.getCustomerName());
                    invoicesTotal.setText(" " + row.getTotal());

                    int s = row.getInvoiceLines().size();
                    lineData = new String[s][5];
                    int count = 0;
                    for (InvoiceLine l : row.getInvoiceLines()){
                        lineData[count][0] = String.valueOf(row.getInvoiceNum());
                        lineData[count][1] = l.getItemName();
                        lineData[count][2] = String.valueOf(l.getItemPrice());
                        lineData[count][3] = String.valueOf(l.getCount());
                        lineData[count][4] = String.valueOf(l.getItemTotal());
                        count++;
                    }

                    lineModel = new DefaultTableModel(lineData, item);
                    invoiceItemsTable.setModel(lineModel);

                    createItem.setEnabled(true);
                    deleteItem.setEnabled(true);
                }
            }
        });

        leftSidePanel.add(new JScrollPane(invoicesTable));

        createNewInvoice = new JButton("Create New Invoice");
        deleteInvoice = new JButton("Delete Invoice");
        leftSidePanel.add(createNewInvoice);
        leftSidePanel.add(deleteInvoice);

    }

    public void createRightSidePanel(){

        JPanel rightSidePanel = new JPanel();
        add(rightSidePanel);

        JPanel container, leftContainer, rightContainer;
        leftContainer = new JPanel();
        leftContainer.add(new JLabel("Invoice Number"));
        leftContainer.add(new JLabel("Invoice Date"));
        leftContainer.add(new JLabel("Customer Name"));
        leftContainer.add(new JLabel("invoice Total"));
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));

        rightContainer = new JPanel();
        rightContainer.add(invoiceNumLabel);
        invoiceDateTF = new JTextField(10);
        rightContainer.add(invoiceDateTF);
        customerNameTF = new JTextField(10);
        rightContainer.add(customerNameTF);
        rightContainer.add(invoicesTotal);
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));

        container = new JPanel();
        container.add(leftContainer);
        container.add(rightContainer);
        container.setLayout(new GridLayout(1,2));

        rightSidePanel.add(container);

        JPanel itemTablePanel = new JPanel();
        itemTablePanel.setBorder(BorderFactory.createTitledBorder("Invoice Items"));

        lineModel = new DefaultTableModel(lineData, item);
        invoiceItemsTable = new JTable(lineModel);

        itemTablePanel.add(new JScrollPane(invoiceItemsTable));
        rightSidePanel.add(itemTablePanel);

        createItem = new JButton("Create Item");
        createItem.setEnabled(false);
        deleteItem = new JButton("Delete Item");
        deleteItem.setEnabled(false);
        rightSidePanel.add(createItem);
        rightSidePanel.add(deleteItem);

    }

    public void headerDialog(){
        headerDialog = new JDialog(this,"Create New Invoice Header");

        JLabel nameDialog = new JLabel("Customer Name");
        customerNameDialog = new JTextField(30);

        JLabel dateDialog = new JLabel("Invoice Date");
        invoiceDateDialog = new JTextField(30);

        okHeader = new JButton("Ok");
        cancelHeader = new JButton("Cancel");

        headerDialog.add(nameDialog);
        headerDialog.add(customerNameDialog);
        headerDialog.add(dateDialog);
        headerDialog.add(invoiceDateDialog);
        headerDialog.add(okHeader);
        headerDialog.add(cancelHeader);

        headerDialog.setVisible(true);
        headerDialog.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        headerDialog.setSize(500,200);
        headerDialog.setLocation(200,200);
        setResizable(false);
        this.setEnabled(false);

        cancelHeader.addActionListener(control);
        okHeader.addActionListener(control);
    }

    public void cancelHeader(){
        headerDialog.setVisible(false);
        this.setEnabled(true);
    }

    public void saveHeader(){
        String[] data = new String[4];
        data[0] = String.valueOf(invoiceModel.getRowCount()+1);
        data[1] = invoiceDateDialog.getText();
        data[2] = customerNameDialog.getText();
        data[3]= "0.0";

        header = new InvoiceHeader();
        header.setInvoiceNum(Integer.parseInt(data[0]));
        header.setInvoiceDate(data[1]);
        header.setCustomerName(data[2]);
        header.setTotal(Double.parseDouble(data[3]));

        control.headers.add(header);

        invoiceModel.addRow(data);
        JOptionPane.showMessageDialog(null, "New row added successfully");
        headerDialog.setVisible(false);
        headerDialog = null;
        this.setEnabled(true);
    }

    public void deleteHeader() {
        int index = invoicesTable.getSelectedRow();
        if (index != -1) {
            invoiceNumLabel.setText(" ");
            invoiceDateTF.setText("");
            customerNameTF.setText("");
            invoicesTotal.setText(" ");

            invoiceModel.removeRow(index);
            lineModel.setRowCount(0);
            control.headers.remove(index);

            JOptionPane.showMessageDialog(null, "Selected row deleted successfully");
        }
    }

    public void lineDialog(){
        lineDialog = new JDialog(this, "Create New Item Line");

        JLabel itemNameDialog = new JLabel("Item Name");
        itemName = new JTextField(30);

        JLabel itemPriceDialog = new JLabel("Item Price");
        itemPrice = new JTextField(30);

        JLabel itemCountDialog = new JLabel("Item Count");
        itemCount = new JTextField(30);

        okLine = new JButton("Ok");
        okLine.setActionCommand("ok line");
        cancelLine = new JButton("Cancel");
        cancelLine.setActionCommand("cancel line");

        lineDialog.add(itemNameDialog);
        lineDialog.add(itemName);
        lineDialog.add(itemPriceDialog);
        lineDialog.add(itemPrice);
        lineDialog.add(itemCountDialog);
        lineDialog.add(itemCount);
        lineDialog.add(okLine);
        lineDialog.add(cancelLine);

        lineDialog.setVisible(true);
        lineDialog.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        lineDialog.setSize(500,200);
        lineDialog.setLocation(200,200);
        setResizable(false);
        this.setEnabled(false);

        cancelLine.addActionListener(control);
        okLine.addActionListener(control);
    }

    public void cancelLine(){
        lineDialog.setVisible(false);
        this.setEnabled(true);
    }

    public void saveLine(){
        String[] data = new String[5];
        data[0] = String.valueOf(row.getInvoiceNum());
        data[1] = itemName.getText();
        data[2] = itemPrice.getText();
        data[3] = itemCount.getText();
        data[4] = String.valueOf(Double.parseDouble(data[2]) * Double.parseDouble(data[3]));

        row.setTotal(row.getTotal()+Double.parseDouble(data[4]));
        invoicesTotal.setText("" + row.getTotal());
        invoicesTable.getModel().setValueAt(row.getTotal(),invoicesTable.getSelectedRow(),3);

        line = new InvoiceLine();
        line.setItemNo(row.getInvoiceNum());
        line.setItemName(data[1]);
        line.setItemPrice(Double.parseDouble(data[2]));
        line.setCount(Integer.parseInt(data[3]));
        line.setItemTotal(Double.parseDouble(data[4]));

        row.getInvoiceLines().add(line);

        lineModel.addRow(data);
        JOptionPane.showMessageDialog(null, "New row added successfully");
        lineDialog.setVisible(false);
        lineDialog = null;
        this.setEnabled(true);
    }

    public void deleteLine(){
        int index = invoiceItemsTable.getSelectedRow();
        if (index != -1) {
            row.setTotal(row.getTotal()-row.getInvoiceLines().get(index).getItemPrice());
            invoicesTotal.setText("" + row.getTotal());
            invoicesTable.getModel().setValueAt(row.getTotal(),invoicesTable.getSelectedRow(),3);

            lineModel.removeRow(index);
            JOptionPane.showMessageDialog(null, "Selected row deleted successfully");
            row.getInvoiceLines().remove(index);
        }
    }

    public void loadFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("choose header file");
        File selectedHeaderFile, selectedLineFile;
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedHeaderFile = fileChooser.getSelectedFile();

            fileChooser.setDialogTitle("choose line file");
            result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedLineFile = fileChooser.getSelectedFile();

                control.headers = file.readFile(selectedHeaderFile.getAbsolutePath(), selectedLineFile.getAbsolutePath());
                String[][] s = control.loadHeader();
                DefaultTableModel model = new DefaultTableModel(s, item);
                invoicesTable.setModel(model);
            }
        }
        invoiceNumLabel.setText(" ");
        invoiceDateTF.setText("");
        customerNameTF.setText("");
        invoicesTotal.setText(" ");

        lineModel.setRowCount(0);

        JOptionPane.showMessageDialog(null, "File Loaded successfully");
    }

    public void saveFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("choose header file to save");
        File selectedHeaderFile, selectedLineFile;
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedHeaderFile = fileChooser.getSelectedFile();

            fileChooser.setDialogTitle("choose line file to save");
            result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedLineFile = fileChooser.getSelectedFile();

                file.writeFile(control.headers, selectedHeaderFile.getAbsolutePath(), selectedLineFile.getAbsolutePath());
            }
        }
        JOptionPane.showMessageDialog(null, "File Saved successfully");
    }

    public void addEventListeners(){

        createNewInvoice.addActionListener(control);
        deleteInvoice.addActionListener(control);
        createItem.addActionListener(control);
        deleteItem.addActionListener(control);

    }
}

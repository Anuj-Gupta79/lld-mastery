package com.lld.patterns.behavioral.templatemethod;

// Abstract class act as template
abstract class Report {

    // LEARNING: generateReport is final method because we don't want subclasses to
    // override it.
    public final void generateReport() {
        fetchData();
        parseData();
        formatReport();
        exportReport();
    }

    // LEARNING: parseData is protected abstract because it should be accessible and
    // overridden by subclasses.
    protected abstract void parseData();

    // LEARNING: formatReport is protected abstract because it should be accessible
    // and overridden by subclasses.
    protected abstract void formatReport();

    // LEARNING: fetchData and exportReport are final because they are common steps
    // for all reports and should not be overridden by subclasses.
    protected final void fetchData() {
        System.out.println("Fetching data from database");
    }

    protected final void exportReport() {
        System.out.println("Exporting report to file");
    }

}

// Subclasses implement the specific steps of the algorithm
class PDFReport extends Report {

    @Override
    protected void parseData() {
        System.out.println("Parsing data for PDF report");
    }

    @Override
    protected void formatReport() {
        System.out.println("Formatting data for PDF report");
    }
}

// Subclasses implement the specific steps of the algorithm
class ExcelReport extends Report {

    @Override
    protected void parseData() {
        System.out.println("Parsing data for Excel report");
    }

    @Override
    protected void formatReport() {
        System.out.println("Formatting data for Excel report");
    }

}

public class TemplateMethodDemo {

    public static void main(String[] args) {
        Report pdfReport = new PDFReport();
        pdfReport.generateReport();

        System.out.println("********************");

        Report excelReport = new ExcelReport();
        excelReport.generateReport();
    }
}

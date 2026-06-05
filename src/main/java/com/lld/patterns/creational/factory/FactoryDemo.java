package com.lld.patterns.creational.factory;

interface Report {
    void generateReport();
}

class PDFReport implements Report {
    @Override
    public void generateReport() {
        System.out.println("Generating PDF report...");
    }
}

class ExcelReport implements Report {
    @Override
    public void generateReport() {
        System.out.println("Generating Excel report...");
    }
}

class CSVReport implements Report {
    @Override
    public void generateReport() {
        System.out.println("Generating CSV report...");
    }
}

// LEARNING: Factory centralizes object creation behind a static method.
// WHY: Caller depends on Report interface, not on
// PDFReport/ExcelReport/CSVReport — loose coupling.
class ReportFactory {
    public static Report createReport(String type) {
        switch (type.toLowerCase()) {
            case "pdf":
                return new PDFReport();
            case "excel":
                return new ExcelReport();
            case "csv":
                return new CSVReport();
            default:
                throw new IllegalArgumentException("Unknown report type: " + type);
        }
    }
}

public class FactoryDemo {
    public static void main(String[] args) {
        Report pdfReport = ReportFactory.createReport("pdf");
        pdfReport.generateReport();

        Report excelReport = ReportFactory.createReport("excel");
        excelReport.generateReport();

        Report csvReport = ReportFactory.createReport("csv");
        csvReport.generateReport();
    }
}
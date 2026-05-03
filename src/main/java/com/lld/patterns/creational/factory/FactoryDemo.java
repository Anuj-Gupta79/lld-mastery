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

// LEARNING: Factory class that creates instances of different report types
// based on input parameters
// WHY: This allows for loose coupling between the client code and the concrete
// report classes, making it easier to add new report types without modifying
// existing code.
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

// LEARNING: Factory pattern is a creational design pattern that provides an
// interface for creating objects in a superclass, but allows subclasses to
// alter the type of objects that will be created.
// Why use Factory pattern?
// 1. To encapsulate the object creation process, allowing for more flexible and
// maintainable code.
// 2. To promote loose coupling between the client code and the concrete
// classes, making it easier to add new types of objects without modifying
// existing code.
// 3. To provide a single point of control for object creation, which can
// simplify code and improve readability.
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

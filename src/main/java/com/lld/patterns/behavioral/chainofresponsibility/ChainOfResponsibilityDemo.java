package com.lld.patterns.behavioral.chainofresponsibility;

// LEARNING: Define enum for ticket priority levels.
enum Priority {
    LOW, MEDIUM, HIGH, CRITICAL;
}

// LEARNING: SupportHandler is the base handler class that defines the structure for handling support tickets.
abstract class SupportHandler {
    protected SupportHandler nextHandler;
   
    // LEARNING: Returns next handler so callers can chain setNextHandler() calls fluently.
    public SupportHandler setNextHandler(SupportHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public void handleRequest(SupportTicket ticket) {
        if (canHandle(ticket)) {
            processRequest(ticket);
        } else if (nextHandler != null) {
            nextHandler.handleRequest(ticket);
        } else {
            System.out.println("Unhandled issue: " + ticket.getIssue());
        }
    }

    protected abstract boolean canHandle(SupportTicket ticket);

    protected abstract void processRequest(SupportTicket ticket);
}

class FrontlineHandler extends SupportHandler {
    @Override
    protected boolean canHandle(SupportTicket ticket) {
        return ticket.getPriority() == Priority.LOW;
    }

    @Override
    protected void processRequest(SupportTicket ticket) {
        System.out.println("Frontline handler processing issue: " + ticket.getIssue());
    }
}

class TeamLeadHandler extends SupportHandler {
    @Override
    protected boolean canHandle(SupportTicket ticket) {
        return ticket.getPriority() == Priority.MEDIUM;
    }

    @Override
    protected void processRequest(SupportTicket ticket) {
        System.out.println("Team lead handler processing issue: " + ticket.getIssue());
    }
}

class ManagerHandler extends SupportHandler {
    @Override
    protected boolean canHandle(SupportTicket ticket) {
        return ticket.getPriority() == Priority.HIGH;
    }

    @Override
    protected void processRequest(SupportTicket ticket) {
        System.out.println("Manager handler processing issue: " + ticket.getIssue());
    }
}

class CXOHandler extends SupportHandler {
    @Override
    protected boolean canHandle(SupportTicket ticket) {
        return ticket.getPriority() == Priority.CRITICAL;
    }

    @Override
    protected void processRequest(SupportTicket ticket) {
        System.out.println("CXO handler processing issue: " + ticket.getIssue());
    }
}

// LEARNING: SupportTicket class represents a support ticket with an issue description and priority level.
class SupportTicket {
    private String issue;
    private Priority priority;

    public SupportTicket(String issue, Priority priority) {
        this.issue = issue;
        this.priority = priority;
    }

    public String getIssue() {
        return issue;
    }

    public Priority getPriority() {
        return priority;
    }
}

public class ChainOfResponsibilityDemo {

    public static void main(String[] args) {
        SupportHandler frontline = new FrontlineHandler();
        SupportHandler teamLead = new TeamLeadHandler();
        SupportHandler manager = new ManagerHandler();
        SupportHandler cxo = new CXOHandler();

        // LEARNING: Chain up the handlers in the order of responsibility.
        frontline.setNextHandler(teamLead).setNextHandler(manager).setNextHandler(cxo);
        SupportTicket ticket1 = new SupportTicket("Password reset", Priority.LOW);
        SupportTicket ticket2 = new SupportTicket("Software installation", Priority.MEDIUM);
        SupportTicket ticket3 = new SupportTicket("System outage", Priority.HIGH);
        SupportTicket ticket4 = new SupportTicket("Data breach", Priority.CRITICAL);
        SupportTicket ticket5 = new SupportTicket("Unknown issue", Priority.CRITICAL);

        frontline.handleRequest(ticket1);
        frontline.handleRequest(ticket2);
        frontline.handleRequest(ticket3);
        frontline.handleRequest(ticket4);
        frontline.handleRequest(ticket5);
    }
}

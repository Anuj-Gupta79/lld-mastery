package com.lld.solid.isp;

interface WorkInterface {
    void work();

    void eat();

    void sleep();

    void manageTeam();

    void signContract();

    void raisePR();
}

class InternViolation implements WorkInterface {

    @Override
    public void work() {
        System.out.println("Intern is working on assigned tasks.");
    }

    @Override
    public void eat() {
        System.out.println("Intern is eating.");
    }

    @Override
    public void sleep() {
        System.out.println("Intern is sleeping.");
    }

    // LEARNING: Large interfaces force clients to implement irrelevant methods.
    // WHY: Interns don't manage teams or sign contracts.
    @Override
    public void manageTeam() {
        throw new UnsupportedOperationException("Intern cannot manage a team.");
    }

    @Override
    public void signContract() {
        throw new UnsupportedOperationException("Intern is not authorized to sign contracts.");
    }

    @Override
    public void raisePR() {
        System.out.println("Intern is responsible for raising PRs.");
    }
}

class ManagerViolation implements WorkInterface {

    @Override
    public void work() {
        System.out.println("Manager is working on assigned tasks.");
    }

    @Override
    public void eat() {
        System.out.println("Manager is eating.");
    }

    @Override
    public void sleep() {
        System.out.println("Manager is sleeping.");
    }

    @Override
    public void manageTeam() {
        System.out.println("Manager is managing the team.");
    }

    @Override
    public void signContract() {
        System.out.println("Manager is signing contracts.");
    }

    @Override
    public void raisePR() {
        throw new UnsupportedOperationException("Manager is not responsible for raising PRs.");
    }
}

interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

interface Manageable {
    void manageTeam();
}

interface ContractSignable {
    void signContract();
}

interface PRRaisable {
    void raisePR();
}

class InternFix implements Workable, Eatable, Sleepable, PRRaisable {

    @Override
    public void work() {
        System.out.println("Intern is working on assigned tasks.");
    }

    @Override
    public void eat() {
        System.out.println("Intern is eating.");
    }

    @Override
    public void sleep() {
        System.out.println("Intern is sleeping.");
    }

    @Override
    public void raisePR() {
        System.out.println("Intern is responsible for raising PRs.");
    }
}

class ManagerFix implements Workable, Eatable, Sleepable, Manageable, ContractSignable {

    @Override
    public void work() {
        System.out.println("Manager is working on assigned tasks.");
    }

    @Override
    public void eat() {
        System.out.println("Manager is eating.");
    }

    @Override
    public void sleep() {
        System.out.println("Manager is sleeping.");
    }

    @Override
    public void manageTeam() {
        System.out.println("Manager is managing the team.");
    }

    @Override
    public void signContract() {
        System.out.println("Manager is signing contracts.");
    }
}

public class InterfaceSegregationPrincipleDemo {

    public static void main(String[] args) {

        // LEARNING: UnsupportedOperationException is often a sign of ISP violation.
        InternViolation internViolation = new InternViolation();

        internViolation.work();
        internViolation.eat();
        internViolation.sleep();

        try {
            internViolation.manageTeam();
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }

        try {
            internViolation.signContract();
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }

        internViolation.raisePR();

        ManagerViolation managerViolation = new ManagerViolation();

        managerViolation.work();
        managerViolation.eat();
        managerViolation.sleep();
        managerViolation.manageTeam();
        managerViolation.signContract();

        try {
            managerViolation.raisePR();
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }

        // LEARNING: Small focused interfaces let classes implement only what they need.
        InternFix intern = new InternFix();

        intern.work();
        intern.eat();
        intern.sleep();
        intern.raisePR();

        ManagerFix manager = new ManagerFix();

        manager.work();
        manager.eat();
        manager.sleep();
        manager.manageTeam();
        manager.signContract();
    }
}
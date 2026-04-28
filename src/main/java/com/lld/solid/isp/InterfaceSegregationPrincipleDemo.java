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

    // Why? Because Intern does not manage a team or sign contracts, but it is
    // forced to implement these methods due to the large interface, leading to a
    // violation of the Interface Segregation Principle.
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

// LEARNINGS:
// 1. The Interface Segregation Principle (ISP) states that clients should not
// be forced to depend on interfaces they do not use. It promotes the idea of
// creating smaller, more specific interfaces rather than large, monolithic
// ones.
// 2. In the violation example, both InternViolation and ManagerViolation
// classes implement the WorkInterface, which contains methods that are not
// relevant to both roles. This leads to unnecessary method implementations and
// violates the ISP.
// 3. In the fix example, we have created separate interfaces for different
// responsibilities (Workable, Eatable, Sleepable, Manageable, ContractSignable,
// PRRaisable). This allows the InternFix and ManagerFix classes to implement
// only the interfaces that are relevant to their roles, adhering to the ISP and
// promoting better code organization and maintainability.
public class InterfaceSegregationPrincipleDemo {

    public static void main(String[] args) {
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

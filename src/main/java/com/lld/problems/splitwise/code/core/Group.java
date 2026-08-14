package com.lld.problems.splitwise.code.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.lld.problems.splitwise.code.constants.PaymentType;
import com.lld.problems.splitwise.code.exceptions.InvalidParticipantException;
import com.lld.problems.splitwise.code.exceptions.InvalidSettlementException;
import com.lld.problems.splitwise.code.exceptions.UnSettledAmountException;
import com.lld.problems.splitwise.code.exceptions.UserAlreadyExistException;
import com.lld.problems.splitwise.code.exceptions.UserNotFoundException;
import com.lld.problems.splitwise.code.models.AuditLog;
import com.lld.problems.splitwise.code.models.Expense;
import com.lld.problems.splitwise.code.models.Notification;
import com.lld.problems.splitwise.code.models.Settlement;
import com.lld.problems.splitwise.code.models.SuggestedSettlement;
import com.lld.problems.splitwise.code.models.User;
import com.lld.problems.splitwise.code.observer.Subject;
import com.lld.problems.splitwise.code.services.AuditLogService;
import com.lld.problems.splitwise.code.services.DebtSimplificationService;
import com.lld.problems.splitwise.code.services.ExpenseLedgerService;
import com.lld.problems.splitwise.code.strategies.ExpenseStrategy;

public class Group implements Subject {
    private String groupId;
    private String groupName;
    private ExpenseLedgerService expenseLedgerService;
    private AuditLogService auditLogService;
    private List<User> users;
    private List<Expense> expenses;
    private List<Settlement> settlements;

    public Group(String id, String name) {
        this.groupId = id;
        this.groupName = name;
        this.expenseLedgerService = new ExpenseLedgerService();
        this.auditLogService = new AuditLogService();
        this.users = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.settlements = new ArrayList<>();
    }

    public Expense addExpense(String actorId, String paidBy, double amount, String description,
            List<String> participantIds, ExpenseStrategy strategy) {
        Set<String> userIds = this.users.stream()
                .map(User::getUserId)
                .collect(Collectors.toSet());

        for (String participantId : participantIds) {
            if (!userIds.contains(participantId)) {
                throw new InvalidParticipantException(
                        "Participant with " + participantId + " is not exist in the user list");
            }
        }

        Expense expense = new Expense(amount, paidBy, description, strategy, participantIds);

        this.expenses.add(expense);

        this.expenseLedgerService.updateBalance(expense);

        AuditLog auditLog = new AuditLog("EXPENSE_ADDED", actorId);
        this.auditLogService.addLog(auditLog);

        notifyObservers(new Notification(auditLog.getDetails(), this.groupId));

        return expense;
    }

    public Settlement recordSettlement(String actorId, String payerId, String payeeId, double amount,
            PaymentType paymentType) {
        double owedAmount = this.expenseLedgerService.getBalance(payerId, payeeId);

        if ((amount - owedAmount) > 0.01) {
            throw new InvalidSettlementException("Paid amount is greater than owedAmount!");
        }

        Settlement settlement = new Settlement(payerId, payeeId, amount, paymentType);
        this.settlements.add(settlement);

        this.expenseLedgerService.updateBalance(settlement);

        AuditLog auditLog = new AuditLog("SETTLEMENT_RECORDED", actorId);
        this.auditLogService.addLog(auditLog);

        notifyObservers(new Notification(auditLog.getDetails(), this.groupId));

        return settlement;
    }

    public void addMember(User user) {
        if (this.users.contains(user)) {
            throw new UserAlreadyExistException(
                    "[Operation Add Member] User with " + user.getUserId() + " already exist in the group.");
        }

        this.users.add(user);
    }

    public void removeMember(User user) {
        if (this.users.contains(user)) {
            if (this.expenseLedgerService.hasOutstandingBalance(user.getUserId())) {
                throw new UnSettledAmountException(
                        "[Operation Remove Member] User has pending settlement in the group");
            } else {
                this.users.remove(user);
                return;
            }
        }

        throw new UserNotFoundException(
                "[Operation Remove Member] User not exist in the group with userId: " + user.getUserId());
    }

    public boolean canBeDeleted() {
        return this.expenseLedgerService.allBalanceZero();
    }

    public List<SuggestedSettlement> simplifyDebt() {
        DebtSimplificationService debtSimplificationService = new DebtSimplificationService();
        return debtSimplificationService.simplify(this);
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public ExpenseLedgerService getExpenseLedgerService() {
        return this.expenseLedgerService;
    }

    public AuditLogService getAuditLogService() {
        return this.auditLogService;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(this.users);
    }

    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(this.expenses);
    }

    public List<Settlement> getSettlements() {
        return Collections.unmodifiableList(this.settlements);
    }

    @Override
    public void notifyObservers(Notification notification) {
        for (User user : this.users) {
            user.update(notification);
        }
    }
}

package com.lld.problems.splitwise.code.core;

import java.util.HashMap;
import java.util.Map;

import com.lld.problems.splitwise.code.exceptions.GroupAlreadyExistException;
import com.lld.problems.splitwise.code.exceptions.GroupNotFoundException;
import com.lld.problems.splitwise.code.exceptions.UnSettledAmountException;
import com.lld.problems.splitwise.code.exceptions.UserAlreadyExistException;
import com.lld.problems.splitwise.code.exceptions.UserNotFoundException;
import com.lld.problems.splitwise.code.models.User;

public class Splitwise {
    private Map<String, User> users;
    private Map<String, Group> groups;

    public Splitwise() {
        this.users = new HashMap<>();
        this.groups = new HashMap<>();
    }

    public void registerUser(User user) {
        if (this.users.containsKey(user.getUserId())) {
            throw new UserAlreadyExistException("Requested User is already registered!");
        }

        this.users.put(user.getUserId(), user);
    }

    public Group createGroup(String id, String name) {
        if (this.groups.containsKey(id)) {
            throw new GroupAlreadyExistException("Requested group already exists!");
        }

        Group group = new Group(id, name);
        this.groups.put(id, group);
        return group;
    }

    public void deleteGroup(String groupId) {
        if (!this.groups.containsKey(groupId)) {
            throw new GroupNotFoundException("Requested Group is not found in the system for deletion!");
        }

        Group group = this.groups.get(groupId);

        if (!group.canBeDeleted()) {
            throw new UnSettledAmountException(
                    "Group cannot be deleted — some members have pending unsettled amounts!");
        }

        this.groups.remove(groupId);
    }

    public void deleteUser(String userId) {
        if (!this.users.containsKey(userId)) {
            throw new UserNotFoundException("Requested User is not found in the system for deletion!");
        }

        for (Group group : this.groups.values()) {
            if (group.getExpenseLedgerService().hasOutstandingBalance(userId)) {
                throw new UnSettledAmountException(
                        "User cannot be deleted — unsettled amount found in group: " + group.getGroupName());
            }
        }

        this.users.remove(userId);
    }
}
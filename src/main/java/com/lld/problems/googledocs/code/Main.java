package com.lld.problems.googledocs.code;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.lld.problems.googledocs.code.constants.RoleType;
import com.lld.problems.googledocs.code.constants.TextStyle;
import com.lld.problems.googledocs.code.core.GoogleDocs;
import com.lld.problems.googledocs.code.exceptions.PermissionDeniedException;
import com.lld.problems.googledocs.code.exceptions.UserNotFoundException;
import com.lld.problems.googledocs.code.models.Comment;
import com.lld.problems.googledocs.code.models.Document;
import com.lld.problems.googledocs.code.models.User;
import com.lld.problems.googledocs.code.operations.DeleteOperation;
import com.lld.problems.googledocs.code.operations.FormatOperation;
import com.lld.problems.googledocs.code.operations.InsertOperation;

public class Main {
    private static int wrongLines = 0;
    private static final GoogleDocs docs = new GoogleDocs();
    private static final User asha = new User("Asha");
    private static final User ravi = new User("Ravi");
    private static final User meena = new User("Meena");
    private static final User kiran = new User("Kiran");
    private static final User zed = new User("Zed");

    public static void main(String[] args) throws Exception {
        for (User u : new User[] { asha, ravi, meena, kiran, zed }) {
            docs.registerUser(u);
        }
        s1();
        s2();
        s3();
        s4();
        s5();
        s6();
        s7();
        s8();
        s9();
        s10();
        s11();
        s12();
        System.out.println("Wrong lines: " + wrongLines);
    }

    // ---------- helpers ----------
    private static void expect(String label, Object actual, Object expected) {
        boolean ok = Objects.equals(expected, actual);
        if (!ok) {
            wrongLines++;
        }
        System.out.println((ok ? "OK    " : "WRONG ") + label
                + " | actual=" + actual + " | expected=" + expected);
    }

    private static void expectThrows(String label, Class<? extends Exception> type, Runnable action) {
        String actual = "no exception";
        try {
            action.run();
        } catch (Exception e) {
            actual = e.getClass().getSimpleName();
        }
        expect(label, actual, type.getSimpleName());
    }

    private static InsertOperation ins(User u, int base, int pos, String text) {
        return new InsertOperation(u.getUserId(), base, pos, text);
    }

    private static DeleteOperation del(User u, int base, int start, int end) {
        return new DeleteOperation(u.getUserId(), base, start, end);
    }

    private static FormatOperation fmt(User u, int base, int start, int end, TextStyle s, boolean on) {
        return new FormatOperation(u.getUserId(), base, start, end, s, on);
    }

    private static Document fresh(String text) {
        Document d = docs.uploadDocument(asha.getUserId(), text, "doc");
        String id = d.getDocumentId();
        docs.shareDocument(asha.getUserId(), id, ravi.getUserId(), RoleType.EDITOR);
        docs.shareDocument(asha.getUserId(), id, meena.getUserId(), RoleType.COMMENTER);
        docs.shareDocument(asha.getUserId(), id, kiran.getUserId(), RoleType.READER);
        return d;
    }

    // ---------- scenarios ----------
    private static void s1() {
        Document blank = docs.createDocument(asha.getUserId(), "blank");
        expect("S1 blank content", blank.getContent(), "");
        expect("S1 blank revision", blank.getRevision(), 0);
        Document up = docs.uploadDocument(asha.getUserId(), "HELLO", "up");
        expect("S1 upload content", up.getContent(), "HELLO");
        expect("S1 upload revision", up.getRevision(), 1);
    }

    private static void s2() {
        Document d = fresh("HELLO");
        String id = d.getDocumentId();
        expectThrows("S2 reader cannot edit", PermissionDeniedException.class,
                () -> docs.edit(id, ins(kiran, 1, 0, "X")));
        expectThrows("S2 commenter cannot edit", PermissionDeniedException.class,
                () -> docs.edit(id, ins(meena, 1, 0, "X")));
        expectThrows("S2 non-member cannot edit", UserNotFoundException.class,
                () -> docs.edit(id, ins(zed, 1, 0, "X")));
        expectThrows("S2 editor cannot share", PermissionDeniedException.class,
                () -> docs.shareDocument(ravi.getUserId(), id, zed.getUserId(), RoleType.READER));
        expectThrows("S2 OWNER role cannot be given", IllegalArgumentException.class,
                () -> docs.shareDocument(asha.getUserId(), id, zed.getUserId(), RoleType.OWNER));
        expectThrows("S2 reader cannot comment", PermissionDeniedException.class,
                () -> d.addComment(new Comment(kiran.getUserId(), 0, 2, "x")));
        expectThrows("S2 bad comment range", IllegalArgumentException.class,
                () -> d.addComment(new Comment(meena.getUserId(), 0, 9, "x")));
        expect("S2 content unchanged", d.getContent(), "HELLO");
        expect("S2 revision unchanged", d.getRevision(), 1);
    }

    private static void s3() {
        Document d = fresh("HELLO");
        docs.edit(d.getDocumentId(), ins(ravi, d.getRevision(), 5, " WORLD"));
        expect("S3 content", d.getContent(), "HELLO WORLD");
        expect("S3 revision", d.getRevision(), 2);
    }

    private static void s4() {
        Document d = fresh("HELLO WORLD");
        String id = d.getDocumentId();
        int base = d.getRevision();
        docs.edit(id, ins(asha, base, 0, "X"));
        docs.edit(id, del(ravi, base, 10, 11));
        expect("S4 content", d.getContent(), "XHELLO WORL");
        expect("S4 revision", d.getRevision(), 3);
    }

    private static void s5() {
        Document d = fresh("HELLO");
        String id = d.getDocumentId();
        AtomicInteger told = new AtomicInteger();
        d.addListener((docId, op) -> told.incrementAndGet());
        int base = d.getRevision();
        docs.edit(id, del(asha, base, 0, 1));
        docs.edit(id, del(ravi, base, 0, 1));
        expect("S5 content", d.getContent(), "ELLO");
        expect("S5 revision", d.getRevision(), 2);
        expect("S5 notifications", told.get(), 1);
    }

    private static void s6() {
        Document d = fresh("HELLO");
        String id = d.getDocumentId();
        int base = d.getRevision();
        docs.edit(id, ins(ravi, base, 3, "Y"));
        docs.edit(id, del(asha, base, 0, 3));
        expect("S6 content", d.getContent(), "YLO");
        expect("S6 revision", d.getRevision(), 3);
    }

    private static void s7() {
        Document d = fresh("HELLO");
        String id = d.getDocumentId();
        docs.edit(id, ins(asha, 1, 5, "A"));
        docs.edit(id, ins(ravi, 2, 6, "B"));
        docs.undo(id, asha.getUserId());
        expect("S7 asha undo removes only her text", d.getContent(), "HELLOB");
        docs.redo(id, asha.getUserId());
        expect("S7 asha redo", d.getContent(), "HELLOAB");
        docs.undo(id, ravi.getUserId());
        expect("S7 ravi undo", d.getContent(), "HELLOA");
        expect("S7 revision", d.getRevision(), 6);
    }

    private static void s8() {
        Document d = fresh("HELLO");
        String id = d.getDocumentId();
        docs.edit(id, ins(asha, 1, 0, "X"));
        docs.undo(id, asha.getUserId());
        docs.edit(id, ins(asha, d.getRevision(), 5, "Y"));
        docs.redo(id, asha.getUserId());
        expect("S8 content", d.getContent(), "HELLOY");
        expect("S8 revision", d.getRevision(), 4);
    }

    private static void s9() {
        Document d = fresh("HELLO WORLD");
        String id = d.getDocumentId();
        docs.edit(id, fmt(asha, d.getRevision(), 0, 5, TextStyle.BOLD, true));
        expect("S9 bold", d.render(), "[BOLD]HELLO[/] WORLD");
        docs.edit(id, fmt(ravi, d.getRevision(), 3, 8, TextStyle.ITALIC, true));
        expect("S9 bold + italic", d.render(), "[BOLD]HEL[BOLD+ITALIC]LO[ITALIC] WO[/]RLD");
        docs.edit(id, fmt(asha, d.getRevision(), 2, 4, TextStyle.BOLD, false));
        expect("S9 remove bold 2-4", d.render(), "[BOLD]HE[/]L[ITALIC]L[BOLD+ITALIC]O[ITALIC] WO[/]RLD");
    }

    private static void s10() {
        Document d = fresh("HELLO WORLD");
        String id = d.getDocumentId();
        d.addComment(new Comment(meena.getUserId(), 6, 11, "nice"));
        d.moveCursor(ravi.getUserId(), 8);
        Comment c = d.getComments().get(0);

        docs.edit(id, ins(asha, d.getRevision(), 0, "ABC"));
        expect("S10 content after insert", d.getContent(), "ABCHELLO WORLD");
        expect("S10 cursor after insert", d.getCursorPosition(ravi.getUserId()), 11);
        expect("S10 comment after insert", c.getStart() + "-" + c.getEnd(), "9-14");

        docs.edit(id, del(asha, d.getRevision(), 0, 3));
        expect("S10 cursor after delete", d.getCursorPosition(ravi.getUserId()), 8);
        expect("S10 comment after delete", c.getStart() + "-" + c.getEnd(), "6-11");

        docs.edit(id, del(ravi, d.getRevision(), 6, 11));
        expect("S10 content after deleting commented text", d.getContent(), "HELLO ");
        expect("S10 cursor inside deleted text", d.getCursorPosition(ravi.getUserId()), 6);
        expect("S10 comment collapsed", c.getStart() + "-" + c.getEnd(), "6-6");
    }

    private static void s11() {
        Document d = fresh("HELLO");
        String id = d.getDocumentId();
        docs.edit(id, ins(asha, 1, 5, " WORLD"));
        docs.edit(id, fmt(asha, 2, 0, 5, TextStyle.BOLD, true));
        expectThrows("S11 reader cannot restore", PermissionDeniedException.class,
                () -> docs.restoreVersion(id, kiran.getUserId(), 1));
        expectThrows("S11 unknown version", IllegalArgumentException.class,
                () -> docs.restoreVersion(id, asha.getUserId(), 99));
        docs.restoreVersion(id, asha.getUserId(), 1);
        expect("S11 content", d.getContent(), "HELLO");
        expect("S11 render", d.render(), "HELLO");
        expect("S11 revision", d.getRevision(), 5);
    }

    private static void s12() throws Exception {
        Document d = fresh("");
        String id = d.getDocumentId();
        AtomicInteger told = new AtomicInteger();
        d.addListener((docId, op) -> told.incrementAndGet());

        int n = 10;
        CountDownLatch start = new CountDownLatch(1);
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            final String letter = String.valueOf((char) ('A' + i));
            final User who = (i % 2 == 0) ? asha : ravi;
            final int base = d.getRevision();
            threads[i] = new Thread(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                docs.edit(id, ins(who, base, 0, letter));
            });
            threads[i].start();
        }
        start.countDown();
        for (Thread t : threads) {
            t.join();
        }

        char[] letters = d.getContent().toCharArray();
        Arrays.sort(letters);
        expect("S12 no letter lost", new String(letters), "ABCDEFGHIJ");
        expect("S12 revision", d.getRevision(), 10);
        expect("S12 notifications", told.get(), 10);
    }
}
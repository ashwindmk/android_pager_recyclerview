package com.ashwin.android.pagerrecyclerview;

import java.util.ArrayList;
import java.util.List;

public class UserDataSource {
    public static List<User> findAll() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "Alice Mayert", "alice@email.com"));
        userList.add(new User(2L, "Bob Kuhic", "bob@email.com"));
        userList.add(new User(3L, "Carrie Kuhn", "carrie@email.com"));
        userList.add(new User(4L, "Deandra Gusikowski", "deandra@email.com"));
        userList.add(new User(5L, "Stephenie Gaylord", "stephenie@email.com"));
        userList.add(new User(6L, "Fruti Madhavan", "fruti@email.com"));

        userList.add(new User(7L, "Alice Mayert", "alice@email.com"));
        userList.add(new User(8L, "Bob Kuhic", "bob@email.com"));
        userList.add(new User(9L, "Carrie Kuhn", "carrie@email.com"));
        userList.add(new User(10L, "Deandra Gusikowski", "deandra@email.com"));
        userList.add(new User(11L, "Stephenie Gaylord", "stephenie@email.com"));
        userList.add(new User(12L, "Fruti Madhavan", "fruti@email.com"));

        userList.add(new User(13L, "Alice Mayert", "alice@email.com"));
        userList.add(new User(14L, "Bob Kuhic", "bob@email.com"));
        userList.add(new User(15L, "Carrie Kuhn", "carrie@email.com"));
        userList.add(new User(16L, "Deandra Gusikowski", "deandra@email.com"));
        userList.add(new User(17L, "Stephenie Gaylord", "stephenie@email.com"));
        userList.add(new User(18L, "Fruti Madhavan", "fruti@email.com"));

        userList.add(new User(19L, "Stephenie Gaylord", "stephenie@email.com"));
        userList.add(new User(20L, "Fruti Madhavan", "fruti@email.com"));
        return userList;
    }
}

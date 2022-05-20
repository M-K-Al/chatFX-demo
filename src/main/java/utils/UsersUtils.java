package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Researcher;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UsersUtils {

    public static final ObservableList<Researcher> USERS = FXCollections.observableArrayList();
    public static Researcher SELF_USER = new Researcher(0 + "",
            "Self",
            "User",
            "self@user.com",
            new Date(),
            new GregorianCalendar(1977, Calendar.JULY, 7).getTime()
    );

    static {
        USERS.add(SELF_USER);

        for (int i = 1; i < 100; i++) {
            USERS.add(new Researcher(i + "",
                    "Mark" + i,
                    "Abra",
                    "mark%d@abra.com".formatted(i),
                    null,
                    new GregorianCalendar(1977, Calendar.JULY, 7).getTime()
            ));
        }
    }

    public static void setSelfUser(final Researcher researcher) {SELF_USER = researcher;}
}

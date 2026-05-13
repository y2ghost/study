package study.ywork.basis.threads;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

public class ReminderService {
    Timer timer = new Timer();

    class Item extends TimerTask {
        String message;

        Item(String m) {
            message = m;
        }

        public void run() {
            message(message);
        }
    }

    public static void main(String[] argv) throws Exception {
        new ReminderService().loadReminders();
    }

    private static final String DATE_PATTERN = "yyyy MM dd hh mm ss";
    private final SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

    protected void loadReminders() throws IOException {
        try (Stream<String> lines = Files.lines(Path.of("ReminderService.txt"))) {
            lines.forEach(aLine -> {
                ParsePosition pp = new ParsePosition(0);
                Date date = formatter.parse(aLine, pp);
                String task = aLine.substring(pp.getIndex());

                if (date == null) {
                    System.out.println("Invalid date in " + aLine);
                    return;
                }

                System.out.println("Date = " + date + "; task = " + task);
                timer.schedule(new Item(task), date);
            });
        }
    }

    void message(String message) {
        System.out.println("\007" + message);
    }
}

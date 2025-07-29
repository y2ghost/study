package study.ywork.jpa.share.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class OneLineLogFormatter extends Formatter {
    public String format(LogRecord logInfo) {
        StringBuilder buf = new StringBuilder(180);
        DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss,SS");
        buf.append("[").append(pad(Thread.currentThread().getName(), 32)).append("] ");
        buf.append(pad(logInfo.getLevel().toString(), 12));
        buf.append(" - ");
        buf.append(pad(dateFormat.format(new Date(logInfo.getMillis())), 24));
        buf.append(" - ");
        buf.append(truncate(logInfo.getLoggerName(), 30));
        buf.append(": ");
        buf.append(formatMessage(logInfo));
        buf.append("\n");

        Throwable throwable = logInfo.getThrown();
        if (throwable != null) {
            StringWriter sink = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sink, true));
            buf.append(sink);
        }

        return buf.toString();
    }

    public String pad(String s, int size) {
        StringBuilder builder = new StringBuilder(s);
        if (s.length() < size) {
            builder.append(" ".repeat(size - s.length()));
        }

        return builder.toString();
    }

    public String truncate(String name, int maxLength) {
        return name.length() > maxLength ? name.substring(name.length() - maxLength) : name;
    }
}
package study.ywork.basis.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ExecuteCmd {
    private static Logger log = Logger.getLogger("ExecuteCmd");

    public static void main(String[] args) {
        List<String> cmdWithArgs = new ArrayList<>();
        cmdWithArgs.add("ls");
        cmdWithArgs.add("-lrt");
        executeCmd(cmdWithArgs, System.out);
    }

    public static void executeCmd(List<String> cmdWithArgs, OutputStream out) {
        long start = System.currentTimeMillis();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmdWithArgs);

        try {
            Process process = processBuilder.start();
            process.getInputStream().transferTo(out);
            process.waitFor();
            int code = process.exitValue();

            if (0 != code) {
                InputStream errorStream = process.getErrorStream();
                String error = new String(errorStream.readAllBytes());
                log.info("执行命令错误: {}" + error);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long end = System.currentTimeMillis();
        log.info(String.format("命令时间消耗: %d", end - start));
    }
}

package study.service;

import msg.service.MsgService;
import java.util.List;

public class AppMain {
    public static void main(String[] args) {
        List<MsgService> msgServices = MsgService.getInstances();
        for (MsgService msgService : msgServices) {
            msgService.showMessage("A test message");
        }
    }
}


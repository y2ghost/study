package msg.provider.swing;

import msg.service.MsgService;
import javax.swing.JOptionPane;

// 演示提供provider函数的服务提供类
public class MsgServiceProvider {
    public static MsgService provider() {
        return new MsgServiceProviderImpl();
    }

    private static class MsgServiceProviderImpl implements MsgService {
        @Override
        public void showMessage(String msg) {
            JOptionPane.showMessageDialog(null, "From MsgServiceProvider: " + msg);
        }
    }
}


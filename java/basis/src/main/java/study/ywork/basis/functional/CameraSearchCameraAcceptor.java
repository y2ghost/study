package study.ywork.basis.functional;

import java.util.ArrayList;
import java.util.List;

public class CameraSearchCameraAcceptor {
    List<Camera> privateListOfCameras = CameraUtils.getList();

    public interface CameraAcceptor {
        boolean choose(Camera c);
    }

    public List<Camera> search(CameraAcceptor acceptor) {
        List<Camera> results = new ArrayList<>();
        for (Camera c : privateListOfCameras) {
            if (acceptor.choose(c)) {
                results.add(c);
            }
        }
        return results;
    }

    public static void main(String[] args) {
        CameraSearchCameraAcceptor searchApp = new CameraSearchCameraAcceptor();
        List<Camera> results = searchApp.search(c -> c.isIlc() && c.price() < 500);
        System.out.println(results);
    }
}

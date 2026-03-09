package study.ywork.basis.functional;

import java.util.List;

public class CameraSearchParallelStream {
    static List<Camera> listOfCameras = CameraUtils.getList();

    public static void main(String[] args) {
        System.out.println("Search Results using For Loop");
        for (Object camera : listOfCameras.parallelStream().
                filter(c -> c.isIlc() && c.price() < 500).
                toArray()) {
            System.out.println(camera);
        }

        System.out.println("Search Results from shorter, more functional approach");
        listOfCameras.parallelStream().
                filter(c -> c.isIlc() && c.price() < 500).
                forEach(System.out::println);
    }
}

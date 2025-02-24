package study.ywork.basis.functional;

public record Camera(
        CameraMake make,
        String model,
        CameraType type,
        int yearIntroduced,
        double price,
        CameraSensor sensorSize,
        double mPix) {
    public boolean isIlc() {
        return type != null && type == CameraType.ILC;
    }

    public boolean isDslr() {
        return type != null && type == CameraType.DSLR;
    }

    public boolean isPointAndShoot() {
        return type != null && type == CameraType.P_S;
    }
}

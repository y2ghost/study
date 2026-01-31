package study.ywork.basis.threads;

public class AutoSave implements Runnable {
    protected FileSaver model;
    public static final int MINUTES = 5;
    private static final int SECONDS = MINUTES * 60;

    public AutoSave(FileSaver m) {
        Thread.currentThread().setName("AutoSave Thread");
        Thread.currentThread().setDaemon(true);
        model = m;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(SECONDS * 1000);
            } catch (InterruptedException e) {
            }
            if (model.wantAutoSave() && model.hasUnsavedChanges())
                model.saveFile(null);
        }
    }
}

interface FileSaver {
    void loadFile(String fn);

    boolean wantAutoSave();

    boolean hasUnsavedChanges();

    void saveFile(String fn);
}

package study.ywork.basis.proxy;

public class MyCacheService implements CacheService {
    private static final String DATA_PREFIX = "CacheData-";

    @Override
    public String getData() {
        return DATA_PREFIX + System.nanoTime();
    }
}

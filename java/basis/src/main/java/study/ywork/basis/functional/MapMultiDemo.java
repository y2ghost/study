package study.ywork.basis.functional;

import java.util.List;

public class MapMultiDemo {
    public static void main(String[] args) {
        List<Double> usPrices = List.of(100d, 250d, 0d, 400d, 1200d, 6000d);
        System.out.println(usPrices);
        List<Double> cdnPrices = usPrices.stream().
                        <Double>mapMulti((price, consumer) -> {
                    if (price > 0) {
                        consumer.accept(price * 1.25);
                    }
                })
                .toList();
        System.out.println(cdnPrices);
    }
}

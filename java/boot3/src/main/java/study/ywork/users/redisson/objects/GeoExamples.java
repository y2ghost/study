package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.GeoEntry;
import org.redisson.api.GeoPosition;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeo;
import org.redisson.api.RedissonClient;
import org.redisson.api.geo.GeoSearchArgs;

import java.util.List;
import java.util.Map;

public class GeoExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RGeo<String> geo = redisson.getGeo("myGeo");
        GeoEntry entry = new GeoEntry(13.361389, 38.115556, "geo1");
        geo.add(entry);
        geo.add(15.087269, 37.502669, "geo2");

        Double dist = geo.dist("geo1", "geo2", GeoUnit.METERS);
        System.out.println(dist);

        Map<String, GeoPosition> pos = geo.pos("geo1", "geo2");
        System.out.println(pos);

        GeoSearchArgs citySearchArgs1 = GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS);
        List<String> cities = geo.search(citySearchArgs1);
        System.out.println(cities);

        GeoSearchArgs citySearchArgs2 = GeoSearchArgs.from("geo1").radius(10, GeoUnit.KILOMETERS);
        List<String> allNearCities = geo.search(citySearchArgs2);
        System.out.println(allNearCities);

        Map<String, GeoPosition> citiesWithDistance = geo.searchWithPosition(citySearchArgs1);
        System.out.println(citiesWithDistance);

        Map<String, GeoPosition> allNearCitiesDistance = geo.searchWithPosition(citySearchArgs2);
        System.out.println(allNearCitiesDistance);

        Map<String, GeoPosition> citiesWithPosition = geo.searchWithPosition(citySearchArgs1);
        System.out.println(citiesWithPosition);

        Map<String, GeoPosition> allNearCitiesPosition = geo.searchWithPosition(citySearchArgs2);
        System.out.println(allNearCitiesPosition);

        redisson.shutdown();
    }
}

package com.ringmabell.whichme_backend.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

@Service
public class GeoService {

    private static final String VEHICLE_KEY = "vehicles";
    private static final String USER_KEY = "users";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 응급차량 위치 추가
    public void addVehicleLocation(String vehicleId, double latitude, double longitude) {
        Point point = new Point(longitude, latitude);
        redisTemplate.opsForGeo().add(VEHICLE_KEY, point, vehicleId);

    }

    // 사용자 위치 추가
    public void addUserLocation(String userId, double latitude, double longitude) {

        redisTemplate.opsForGeo().add(USER_KEY, new Point(longitude, latitude), userId);
    }

    // 특정 위치 근처의 사용자 조회
    public List<String> findNearbyUsers(double longitude, double latitude, double radius) {
        // radius는 미터 단위
        GeoResults<GeoLocation<Object>> results = redisTemplate.opsForGeo()
                .radius(USER_KEY, new Point(longitude, latitude), radius);

        return results.getContent().stream()
                .map(geoResult -> geoResult.getContent().getName().toString())
                .collect(Collectors.toList());
    }

    // 특정 위치 근처의 차량 조회
    public List<String> findNearbyVehicles(double longitude, double latitude, double radius) {
        // radius는 미터 단위

        Circle searchArea = new Circle( new Point(longitude, latitude), new Distance(radius, Metrics.METERS));
        GeoResults<GeoLocation<Object>> results = redisTemplate.opsForGeo()
                .radius(USER_KEY, searchArea);

        return results.getContent().stream()
                .map(geoResult -> (String)geoResult.getContent().getName())
                .collect(Collectors.toList());
    }
}
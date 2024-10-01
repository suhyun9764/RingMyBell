package com.ringmabell.whichme_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ringmabell.whichme_backend.entitiy.dispatch.Station;
import com.ringmabell.whichme_backend.entitiy.dispatch.SubUnit;
import com.ringmabell.whichme_backend.repository.StationRepository;
import com.ringmabell.whichme_backend.repository.SubUnitRepository;

@Component
public class DataLoader implements CommandLineRunner {
    private final StationRepository stationRepository;
    private final SubUnitRepository subUnitRepository;

    public DataLoader(StationRepository stationRepository, SubUnitRepository subUnitRepository) {
        this.stationRepository = stationRepository;
        this.subUnitRepository = subUnitRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 소방서 및 경찰서 데이터 저장
        Station songpaFireStation = new Station();
        songpaFireStation.setName("송파소방서");
        songpaFireStation.setType("소방서");
        songpaFireStation.setLocation("서울 송파구");
        stationRepository.save(songpaFireStation);

        Station gangnamPoliceStation = new Station();
        gangnamPoliceStation.setName("강남경찰서");
        gangnamPoliceStation.setType("경찰서");
        gangnamPoliceStation.setLocation("서울 강남구");
        stationRepository.save(gangnamPoliceStation);

        // 하위 단위 데이터 저장
        SubUnit garak119 = new SubUnit();
        garak119.setStation(songpaFireStation);
        garak119.setName("가락119구조대");
        subUnitRepository.save(garak119);

        SubUnit jamsil119 = new SubUnit();
        jamsil119.setStation(songpaFireStation);
        jamsil119.setName("잠실119구조대");
        subUnitRepository.save(jamsil119);

        SubUnit gangnamSpecialTeam = new SubUnit();
        gangnamSpecialTeam.setStation(gangnamPoliceStation);
        gangnamSpecialTeam.setName("강남특수수사팀");
        subUnitRepository.save(gangnamSpecialTeam);
    }
}

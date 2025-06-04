package com.dell.startup.repositories;

import com.dell.startup.dto.StartupStatsDTO;
import com.dell.startup.models.Startup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    @Query("""
    SELECT new com.dell.startup.dto.StartupStatsDTO(
        s.id,
        s.name,
        s.points,
        SUM(CASE WHEN e.type = 'CONVINCING_PITCH' THEN 1 ELSE 0 END),
        SUM(CASE WHEN e.type = 'BUGGY_PRODUCT' THEN 1 ELSE 0 END),
        SUM(CASE WHEN e.type = 'GOOD_USER_TRACTION' THEN 1 ELSE 0 END),
        SUM(CASE WHEN e.type = 'ANGRY_INVESTOR' THEN 1 ELSE 0 END),
        SUM(CASE WHEN e.type = 'FAKE_NEWS_IN_PITCH' THEN 1 ELSE 0 END)
    )
    FROM Startup s
    LEFT JOIN Event e ON e.startup = s
    GROUP BY s.id, s.name, s.points
""")
List<StartupStatsDTO> fetchStartupStats();

}

package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.enums.Children;
import com.example.ludogoriesoft.village.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {
    @Query("SELECT v FROM Village v WHERE v.name LIKE %:keyword%")
    List<Village> findByName(@Param("keyword") String keyword);

    @Query(value = "SELECT DISTINCT v FROM Village v " +
            "JOIN v.objectVillages ov " +
            "JOIN ov.object o " +
            "JOIN v.villageLivingConditions vl " +
            "JOIN vl.livingCondition lc " +
            "JOIN v.population p " +
            "WHERE o.type IN :objectTypes " +
            "AND lc.livingConditionName IN :livingConditionNames " +
            "AND p.children = :childrenCount " +
            "AND ov.distance = 'IN_THE_VILLAGE' " +
            "AND vl.consents = 'COMPLETELY_AGREED' " +
            "GROUP BY v.name")
    List<Village> searchVillages(@Param("objectTypes") List<String> objectAroundVillageDTOS,
                                 @Param("livingConditionNames") List<String> livingConditionDTOS,
                                 @Param("childrenCount") Children children);


}

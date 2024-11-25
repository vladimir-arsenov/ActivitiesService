package org.example.tfintechgradproject.repository;

import org.example.tfintechgradproject.model.ActivityRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRequestRepository extends JpaRepository<ActivityRequest, Long> {

    @Query(value = """
        select * from activity_request
        where ST_DWithin(coordinates, ST_GeometryFromText('POINT(:coordinates)'), :radius)
        order by ST_Distance(
        	ST_GeometryFromText('POINT(:coordinates)'),
        	coordinates
        )
""")
    List<ActivityRequest> getClosest(@Param("coordinates") String coordinates,
                                     @Param("radius")  Double radius);
}

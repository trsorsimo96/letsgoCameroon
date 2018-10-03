package cm.com.letsgo.repository;

import cm.com.letsgo.domain.Resa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Resa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResaRepository extends JpaRepository<Resa, Long> {

}

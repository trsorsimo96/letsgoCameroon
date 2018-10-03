package cm.com.letsgo.repository;

import cm.com.letsgo.domain.ConfigCommission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConfigCommission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigCommissionRepository extends JpaRepository<ConfigCommission, Long> {

}

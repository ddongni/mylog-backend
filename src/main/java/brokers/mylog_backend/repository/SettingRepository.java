package brokers.mylog_backend.repository;

import brokers.mylog_backend.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Setting findByUserId(long id);
}

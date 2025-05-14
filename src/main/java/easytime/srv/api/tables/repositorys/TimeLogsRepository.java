package easytime.srv.api.tables.repositorys;

import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TimeLogsRepository extends JpaRepository<TimeLog, Integer> {
    Optional<TimeLog> findByUserAndData(User user, LocalDate data);
}

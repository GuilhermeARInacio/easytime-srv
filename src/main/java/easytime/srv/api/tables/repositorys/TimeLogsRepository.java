package easytime.srv.api.tables.repositorys;

import easytime.srv.api.model.pontos.EntradaESaida;
import easytime.srv.api.tables.Appointment;
import easytime.srv.api.tables.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TimeLogsRepository extends JpaRepository<TimeLog, Integer> {

}

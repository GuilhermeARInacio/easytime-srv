package easytime.srv.api.tables.repositorys;

import easytime.srv.api.tables.PasswordValidationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordValidationCodeRepository extends JpaRepository<PasswordValidationCode, Integer> {
//    Optional<PasswordValidationCode> findById(Integer id);
    Optional<PasswordValidationCode> findByCode(String code);
}

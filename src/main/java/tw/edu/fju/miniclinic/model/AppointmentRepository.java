package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByApptDate(LocalDate apptDate);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctorAndApptDate(Doctor doctor, LocalDate apptDate);
    long countByApptDateBetween(LocalDate from, LocalDate to);
    long countByStatus(String status);

    @Query("SELECT a.doctor.department AS department, COUNT(a) AS count " +
            "FROM Appointment a " +
            "GROUP BY a.doctor.department " +
            "ORDER BY a.doctor.department")
    List<DepartmentCount> countByDoctorDepartment();

    interface DepartmentCount {
        String getDepartment();
        long getCount();
    }
}
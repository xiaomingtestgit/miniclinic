package tw.edu.fju.miniclinic.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Component
public class OutDatedPatientRepository {
    private static final List<Patient> PATIENTS = Arrays.asList(
        new Patient("TEST001", "張小明", "男", null, "1145141919"),
        new Patient("TEST002", "李美麗", "女", null, "1234567890"),
        new Patient("TEST003", "王大華", "男", null, "1234567890"),
        new Patient("TEST004", "陳小英", "女", null, "1234567890")
    );
    public List<Patient> findAll() {
        return PATIENTS;
    }
    public Optional<Patient> findByChartNo(String ChartNo) {
        return PATIENTS.stream()
            .filter(p -> p.getChartNo().equals(ChartNo))
            .findFirst();
    }
    public List<Patient> findByGender(String gender) {
        return PATIENTS.stream()
            .filter(p -> p.getGender().equals(gender))
            .toList();
    }
    public List<String> findAllGender() {
        return PATIENTS.stream()
            .map(Patient::getGender)
            .distinct()
            .toList();
    }
}

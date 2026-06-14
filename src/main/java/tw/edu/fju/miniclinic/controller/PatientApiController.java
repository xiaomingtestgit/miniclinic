package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;
@RestController
public class PatientApiController {

    @Autowired
    private PatientRepository patientRepo;

    @GetMapping("/api/patients")
    public List<Patient> getPatients(
            @RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            return patientRepo.findAll();
        }
        return patientRepo.findByName(name);
    }

    // @GetMapping("/api/patients/{chartNo}")
    // public ResponseEntity<Patient> getPatient(@PathVariable String chartNo) {
    //     Optional<Patient> patient = patientRepo.findByChartNo(chartNo);
    //     return patient
    //         .map(p -> ResponseEntity.ok(p))       // 有 → 200 OK + 資料
    //         .orElse(ResponseEntity.notFound().build());  // 沒有 → 404
    // }

   // @GetMapping("/api/patients/genders")
  //  public List<String> getGenders() {
   //     return patientRepo.findAllGender();
  //  }
}
package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;

@Controller
public class PatientPageController {

    @Autowired
    private PatientRepository patientRepo;

    @GetMapping("/patients")
    public String listPatients(
            @RequestParam(required = false) String name,
            Model model) {

        List<Patient> patients;
        if (name == null || name.isBlank()) {
            patients = patientRepo.findAll();
        } else {
            patients = patientRepo.findByName(name);
        }

        model.addAttribute("patients", patients);
       // model.addAttribute("genders", patientRepo.findAllGender());
        model.addAttribute("selectedName", name);

        return "patients";
    }

        // @GetMapping("/patients/{chartNo}")
        //     public String patientDetail(@PathVariable String chartNo, Model model) {
        //         Optional<Patient> patient = patientRepo.findByChartNo(chartNo);

        //         if (patient.isEmpty()) {
        //             return "redirect:/patients";  // 找不到就回清單頁
        //         }

        //         model.addAttribute("patient", patient.get());
        //         return "patient-detail";
        //     }
}

package tw.edu.fju.miniclinic.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

@RestController
public class StatApiController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/api/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("doctorCount", doctorRepo.count());
        response.put("patientCount", patientRepo.count());
        response.put("appointmentCount", appointmentRepo.count());

        Map<String, Long> statusCounts = new LinkedHashMap<>();
        statusCounts.put("BOOKED", appointmentRepo.countByStatus("BOOKED"));
        statusCounts.put("COMPLETED", appointmentRepo.countByStatus("COMPLETED"));
        statusCounts.put("CANCELLED", appointmentRepo.countByStatus("CANCELLED"));
        response.put("statusCounts", statusCounts);
        response.put("departmentCounts", appointmentRepo.countByDoctorDepartment());

        return response;
    }
}

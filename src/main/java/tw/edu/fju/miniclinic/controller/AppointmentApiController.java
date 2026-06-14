package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AppointmentApiController {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @GetMapping("/api/appointments/count")
    public Map<String, Long> getAppointmentCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", appointmentRepo.count());
        return response;
    }

    @GetMapping("/api/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String doctorId) {

        List<Appointment> appointments;

        if (doctorId != null && !doctorId.isBlank()) {
            Optional<Doctor> doctor = doctorRepo.findById(doctorId);
            if (doctor.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            appointments = appointmentRepo.findByDoctor(doctor.get());
            if (date != null && !date.isBlank()) {
                try {
                    LocalDate parsedDate = LocalDate.parse(date);
                    appointments = appointments.stream()
                            .filter(a -> parsedDate.equals(a.getApptDate()))
                            .collect(Collectors.toList());
                } catch (DateTimeParseException ex) {
                    return ResponseEntity.badRequest().build();
                }
            }
            return ResponseEntity.ok(appointments);
        }

        if (date != null && !date.isBlank()) {
            try {
                LocalDate parsedDate = LocalDate.parse(date);
                appointments = appointmentRepo.findByApptDate(parsedDate);
            } catch (DateTimeParseException ex) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(appointments);
        }

        appointments = appointmentRepo.findAll();
        return ResponseEntity.ok(appointments);
    }
    @PutMapping("/api/appointments/{apptId}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long apptId,
            @RequestBody Map<String, String> payload,
            HttpSession session) {

        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");

        Appointment appt = appointmentRepo.findById(apptId).orElse(null);
        if (appt == null) {
            return ResponseEntity.notFound().build();
        }

        // 只能修改自己的掛號
        if (!appt.getDoctor().getDoctorId().equals(loggedInDoctorId)) {
            return ResponseEntity.status(403).build();
        }

        String newStatus = payload.get("status");
        if (!List.of("BOOKED", "COMPLETED", "CANCELLED").contains(newStatus)) {
            return ResponseEntity.badRequest().build();
        }

        appt.setStatus(newStatus);
        return ResponseEntity.ok(appointmentRepo.save(appt));
    }
}

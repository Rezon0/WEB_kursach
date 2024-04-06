package com.example.russionTest;

import com.example.russionTest.Services.*;
import com.example.russionTest.domain.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class GreetingController {
    private UserService userService;
    private VacCertService vacCertService;
    private VaccinationService vaccinationService;
    private VacAndCertService vacAndCertService;
    private SchoolClassService schoolClassService;
    private String lastnameInput;
    private String firstnameInput;
    private String patronymicInput;
    private String calendarInput;

    String selectNameVaccination;
    String facticalVaccination;
    String reaction;

    private User tempUser;

    @Autowired
    public void setSchoolClassService(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }

    @Autowired
    public void setVacAndCertService(VacAndCertService vacAndCertService) {
        this.vacAndCertService = vacAndCertService;
    }

    @Autowired
    public void setVaccinationService(VaccinationService vaccinationService) {
        this.vaccinationService = vaccinationService;
    }

    @Autowired
    public void setVacCertService(VacCertService vacCertService) {
        this.vacCertService = vacCertService;
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private HtmlDownloader htmlDownloader;


    @GetMapping("/")
    public String greeting(Map<String, Object> model)
    {
        return "redirect:/temp";
    }

    @GetMapping("/temp")
    public String tempForm(Map<String, Object> model, Principal principal) {

        if (principal != null){
            String authority = userService.getAuthorityByusername(principal.getName());

            System.out.println(authority);

            if(authority.equals("ROLE_ADMIN")){
                return "redirect:/adminForm";
            }
            if(authority.equals("ROLE_USER")){
                return "redirect:/studentForm";
            }
        }

        return null;
    }

    @GetMapping("/studentForm")
    public String successfulStudentLogin(Model model, Authentication authentication){
        User user = userService.getUserByUserName(authentication.getName());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("firstname", user.getFirstname());
        model.addAttribute("lastname", user.getLastname());
        model.addAttribute("patronymic", user.getPatronymic());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("url", user.getUrl());


        lastnameInput = null;
        firstnameInput = null;
        patronymicInput = null;
        calendarInput = null;

        selectNameVaccination = null;
        facticalVaccination = null;
        reaction = null;

        tempUser = null;

        return "studentForm";
    }

    @PostMapping("/studentForm/studentVaccinationCalendar")
    public String studentVaccinationCalendar(Model model) {
        return "redirect:/studentForm/studentVaccinationCalendar_temp";
    }

    @GetMapping("/studentForm/studentVaccinationCalendar_temp")
    public String getClientProfile(Model model, Authentication authentication) {

        User user = userService.getUserByUserName(authentication.getName());

        List<Vaccination> vacList = vaccinationService.getVaccinationsByCertifficateId(vacCertService.getVacCertByUsername(user));

        List<VaccinationCalendar> vaccinationCalendarList = new ArrayList<>();

        try {
            for (Vaccination item: vacList) {
                VaccinationCalendar vaccinationCalendar = new VaccinationCalendar();
                vaccinationCalendar.setLastname(user.getLastname());
                vaccinationCalendar.setFirstname(user.getFirstname());
                vaccinationCalendar.setPatronymic(user.getPatronymic());
                vaccinationCalendar.setBirthday(user.getBirthday().toLocalDate());
                vaccinationCalendar.setSchoolClassName(user.getSchoolClass().getName());
                vaccinationCalendar.setVacName(item.getName());


                if (item.getInterval() != null)
                    vaccinationCalendar.setDatePlanVaccine(user.getBirthday().toLocalDate().plus(Period.parse(item.getInterval())));
                else
                    vaccinationCalendar.setDatePlanVaccine(LocalDate.MIN);

//                if (vaccinationCalendar.getDatePlanVaccine().isAfter(LocalDate.now().minusDays(14)) &&
//                        vaccinationCalendar.getDatePlanVaccine().isBefore(LocalDate.now().minusDays(7))) {
//                    vaccinationCalendar.setHighlighted("yellowRow");
//                } else if (vaccinationCalendar.getDatePlanVaccine().isBefore(LocalDate.now().minusDays(7))) {
//                    vaccinationCalendar.setHighlighted("redRow");
//                }else{
//                    vaccinationCalendar.setHighlighted("");
//                }

                vaccinationCalendar.setHighlighted("");

                VaccinationAndCertificate vac = vacAndCertService.getVaccinationAndCertificateByVaccinationIdAndVaccinationCertifficateId(item.getId(), vacCertService.getVacCertByUsername(user).getId());

                vaccinationCalendar.setDateFactVaccine(vac.getDateVaccination().toLocalDate());
                vaccinationCalendar.setReaction(vac.getReaction());

                vaccinationCalendarList.add(vaccinationCalendar);

            }
        } catch (NullPointerException e){
            System.out.println(e.getStackTrace());
        }

        System.out.println();

        model.addAttribute("users", vaccinationCalendarList);

       // model.addAttribute("users", user);
        return "studentVaccinationCalendar"; // This is the Thymeleaf template name
    }


//    @PostMapping("/adminFormPost")
//    public String studentVaccinationCalendarPost(Model model) {
//        return "adminForm";
//    }


    @GetMapping("/adminForm")
    public String successfulAdminLogin(Model model, Authentication authentication){
        User user = userService.getUserByUserName(authentication.getName());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("firstname", user.getFirstname());
        model.addAttribute("lastname", user.getLastname());
        model.addAttribute("patronymic", user.getPatronymic());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("url", user.getUrl());


        lastnameInput = null;
        firstnameInput = null;
        patronymicInput = null;
        calendarInput = null;

        selectNameVaccination = null;
        facticalVaccination = null;
        reaction = null;

        tempUser = null;


        return "adminForm";
    }

    @PostMapping("/adminForm/adminVaccinationCalendar")
    public String adminVaccinationCalendar(Model model) {
        return "redirect:/adminForm/adminVaccinationCalendar_temp";
    }

    @GetMapping("/adminForm/adminVaccinationCalendar_temp")
    public String getAdminCalendar(Model model, Authentication authentication) {
        List<SchoolClass> schoolClass = schoolClassService.getAllSchoolClass();
        List<Vaccination> vaccination = vaccinationService.getAllVaccinations();
        List<VaccinationCalendar> vaccinationCalendarList = new ArrayList<>();
        model.addAttribute("disabled", true);




    if (tempUser != null)
    {
        try {
        List<Vaccination> vacList = vaccinationService.getVaccinationsByCertifficateId(vacCertService.getVacCertByUsername(tempUser));

        for (Vaccination item: vacList) {
            VaccinationCalendar vaccinationCalendar = new VaccinationCalendar();
            vaccinationCalendar.setLastname(tempUser.getLastname());
            vaccinationCalendar.setFirstname(tempUser.getFirstname());
            vaccinationCalendar.setPatronymic(tempUser.getPatronymic());
            vaccinationCalendar.setVacCertId(vacCertService.getVacCertByUsername(tempUser).getId());
            vaccinationCalendar.setBirthday(tempUser.getBirthday().toLocalDate());
            vaccinationCalendar.setSchoolClassName(tempUser.getSchoolClass().getName());
            vaccinationCalendar.setVacName(item.getName());
            if (item.getInterval() != null)
                vaccinationCalendar.setDatePlanVaccine(tempUser.getBirthday().toLocalDate().plus(Period.parse(item.getInterval())));
            else
                vaccinationCalendar.setDatePlanVaccine(LocalDate.MIN);

            VaccinationAndCertificate vac = vacAndCertService.getVaccinationAndCertificateByVaccinationIdAndVaccinationCertifficateId(item.getId(), vacCertService.getVacCertByUsername(tempUser).getId());

            vaccinationCalendar.setDateFactVaccine(vac.getDateVaccination().toLocalDate());
            vaccinationCalendar.setReaction(vac.getReaction());

            vaccinationCalendarList.add(vaccinationCalendar);

        }
        }
        catch (NullPointerException e){
            System.out.println(e.getStackTrace());
        }
        model.addAttribute("vaccinations", vaccination);
        model.addAttribute("disabled", false);
    }


        model.addAttribute("lastnameInputTemp", lastnameInput);
        model.addAttribute("firstnameInputTemp", firstnameInput);
        model.addAttribute("patronymicInputTemp", patronymicInput);
        model.addAttribute("calendarInputTemp", calendarInput);

//        lastnameInput = null;
//        firstnameInput = null;
//        patronymicInput = null;
//        calendarInput = null;


        model.addAttribute("users", vaccinationCalendarList);

        model.addAttribute("schoolClasses", schoolClass);


        return "adminVaccinationCalendar"; // This is the Thymeleaf template name
    }

    @GetMapping("/adminForm/adminVaccinationCalendarFilter_temp")
    public String getAdminCalendarFilter(Model model, Authentication authentication) {

        return "adminVaccinationCalendar"; // This is the Thymeleaf template name
    }

    @PostMapping("/adminForm/editStudentsFilter")
    public String successfulRecord(@RequestParam("schoolClassFilter") String schoolClassFilter,
                                   @RequestParam("lastnameInput") String lastnameInput,
                                   @RequestParam("firstnameInput") String firstnameInput,
                                   @RequestParam("patronymicInput") String patronymicInput,
                                   @RequestParam("calendarInput") String calendarInput,
                                   Authentication authentication) {

            tempUser = userService.getByLastnameAndAndFirstnameAndPatronymicAndBirthdayAndSchoolClassName(
                    lastnameInput, firstnameInput, patronymicInput, Date.valueOf(calendarInput), schoolClassFilter);

            this.lastnameInput = lastnameInput;
            this.firstnameInput = firstnameInput;
            this.patronymicInput = patronymicInput;
            this.calendarInput = calendarInput;


            return "redirect:/adminForm/adminVaccinationCalendar_temp";
    }

    @PostMapping("/adminForm/editStudentsSaveVac")
    public String adminVaccinationCalendarSaveVac(@RequestParam("selectNameVaccination") String selectNameVaccination,
                                                  @RequestParam("facticalVaccination") String facticalVaccination,
                                                  @RequestParam("reaction") String reaction,
                                                  Authentication authentication) {

        this.selectNameVaccination = selectNameVaccination;
        this.facticalVaccination = facticalVaccination;
        this.reaction = reaction;

        return "redirect:/adminForm/editStudentsSaveVac_temp";
    }

    @GetMapping("/adminForm/editStudentsSaveVac_temp")
    public String getAdminCalendarSaveVacTemp(Model model, Authentication authentication) {

        Vaccination vaccination = vaccinationService.getVaccinationByName(selectNameVaccination);

        if(vaccination != null){
            if (Date.valueOf(facticalVaccination).compareTo(tempUser.getBirthday()) > 0)
            {
            vacAndCertService.saveVaccinationAndCertificate(
                        vaccination,
                        vacCertService.getVacCertByUsername(tempUser),
                        facticalVaccination,
                        reaction);
            }
        }

        return "redirect:/adminForm/adminVaccinationCalendar_temp";
    }

}
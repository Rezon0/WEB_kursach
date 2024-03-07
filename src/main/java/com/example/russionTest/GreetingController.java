package com.example.russionTest;

import com.example.russionTest.Services.TattooService;
import com.example.russionTest.Services.UserService;
import com.example.russionTest.domain.*;
import com.example.russionTest.repos.MessageRepo;
import com.example.russionTest.repos.TattooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.security.Principal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class GreetingController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GreetingController(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    private UserDetailsService userDetailsService;

    @Autowired
    private TattooService tattooService;

    private TattooRepository tattooRepository;

    private UserService userService;

    private static final String URL = "jdbc:postgresql://localhost:5432/testdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    private void addUserToDB(User user, Authority authority, String role){

        // ТУТ ШИФРОВАНИЕ ПАРОЛЯ В БД

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ТУТ ВСТАВКА В БД ЮЗЕРА

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getDescription());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setBoolean(4, user.getEnabled());
                preparedStatement.setString(5, user.getFirstname());
                preparedStatement.setString(6, user.getGender());
                preparedStatement.setString(7, user.getLastname());
                preparedStatement.setString(8, user.getPassword());
                preparedStatement.setString(9, user.getPatronymic());
                preparedStatement.setString(10, user.getPhone());
                preparedStatement.setString(11, user.getUrl());
                preparedStatement.executeUpdate();
            }

            // А ТУТ РОЛЕЙ
            query = "INSERT INTO authorities VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, authority.getId());
                preparedStatement.setString(2, role);
                preparedStatement.setString(3, user.getUsername());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addRecordToDB(Records records){

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO record VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, records.getId());
                preparedStatement.setObject(2, records.getDateTime());
                preparedStatement.setString(3, records.getPlace());
                preparedStatement.setString(4, records.getMaster().getUsername());
                preparedStatement.setInt(5, records.getTattooCatalog().getId());
                preparedStatement.setString(6, records.getUser().getUsername());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model)
    {
        return "redirect:/temp";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String text,
                      @RequestParam String tag,
                      Map<String, Object> model,
                      Principal principal){
       Message message = new Message(text, tag);
       messageRepo.save(message);

       Iterable<Message> messages = messageRepo.findAll();
       model.put("messages", messages);


        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model){
        Iterable<Message> messages;
        if(filter != null && !filter.isEmpty()){
            messages = messageRepo.findByTag(filter);
        }else{
            messages = messageRepo.findAll();
        }

        model.put("messages", messages);
        return "main";
    }


    @PostMapping("/client_menu")
    public String clientMenu(Map<String, Object> model, Principal principal)
    {
        return "client_menu";
    }

    @PostMapping("/authenticateTheUser")
    public String authenticateUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails != null) {
            String storedPassword = userDetails.getPassword();
            if (password.equals(storedPassword)) {
                model.addAttribute("username", username);
                return "redirect:/temp";
            }
        }
        return "login";
    }

    @PostMapping("/registrationUser")
    public String registrationUser(Model model) {
        return "redirect:/registrationUser/tempRegistration";
    }

    @GetMapping("/registrationUser/tempRegistration")
    public String registrationTemp(Model model) {
        model.addAttribute("user", new User());
        return "registrationUser";
    }


    @PostMapping("/registrationUser/successfulRegistrationUser")
    public String successfulRegistrationUser(Model model, User user) {
        Authority authority;
        user.setEnabled(true);
        user.setUrl("/icon.png");
        authority = new Authority();
        authority.setAuthority("ROLE_USER");

        // ТУТ КОСТЫЛЬ НА УВЕЛИЧЕНИЕ ID authority НА 1 (А-ЛЯ serial)

        String QUERY = "SELECT max(id) FROM authorities";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
        ) {
            while(rs.next()){
                authority.setId(rs.getInt("max")+1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        user.setAuthority(authority);
        authority.setUser(user);

        addUserToDB(user, authority, "ROLE_USER");



        return "login";
    }


    @GetMapping("/admin_form")
    public String adminForm(Map<String, Object> model) {

        return "admin_form";
    }

    @GetMapping("/client_menu")
    public String clientForm(Map<String, Object> model) {

        return "client_menu";
    }

    @GetMapping("/temp")
    public String tempForm(Map<String, Object> model, Principal principal) {

        if (principal != null){
            String authority = userService.getAuthorityByusername(principal.getName());

            System.out.println(authority);

            if(authority.equals("ROLE_ADMIN")){
                return "redirect:/admin_form";
            }
            if(authority.equals("ROLE_USER")){
                return "redirect:/client_menu";
            }
        }

        return "admin_form";
    }

    @PostMapping("/client_menu/profile")
    public String clientProfile(Model model) {
        return "redirect:/client_menu/temp_profile";
    }

    @GetMapping("/client_menu/temp_profile")
    public String getClientProfile(Model model, Authentication authentication) {

        User user = userService.getUserByUserName(authentication.getName());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("firstname", user.getFirstname());
        model.addAttribute("lastname", user.getLastname());
        model.addAttribute("patronymic", user.getPatronymic());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("url", user.getUrl());

        return "clientProfile"; // This is the Thymeleaf template name
    }



    @PostMapping("/client_menu/view_records")
    public String clientViewRecords(Model model) {
        return "redirect:/client_menu/temp_view_records";
    }

    @GetMapping("client_menu/temp_view_records")
    public String getClientRecords(Model model, Authentication authentication) {
        String sqlQuery = "SELECT tc.url  as \"url\", tc.name as \"name\", r.date_time as \"date\", tc.price as \"price\", " +
                          "m.lastname  as \"lastname\", m.firstname  as \"firstname\", m.patronymic  as \"patronymic\", " +
                          "r.place as \"place\" FROM record r " +
                          "join tattoo_catalog tc on r.tattoo_catalog_id = tc.id " +
                          "join users u on r.username_client = u.username " +
                          "join users m on r.username_master = m.username " +
                          "where r.username_client = '" + authentication.getName() + "'";

        List<Map<String, Object>> records = jdbcTemplate.queryForList(sqlQuery);


        model.addAttribute("records", records);

        //model.addAttribute("masters", master);

        return "clientRecords";
    }


    @PostMapping("/client_menu/tattoo_zapis")
    public String clientZapis(Model model) {
        return "redirect:/client_menu/temp_tattoo_zapis";
    }

    @GetMapping("client_menu/temp_tattoo_zapis")
    public String getAllZapis(Model model) {
        String sqlQuery = "SELECT name, price, url FROM tattoo_catalog";
        List<Map<String, Object>> tattoo = jdbcTemplate.queryForList(sqlQuery);

        sqlQuery = "SELECT lastname, firstname, patronymic FROM users u " +
                    "inner join authorities a on a.username = u.username " +
                    "where a.authority = 'ROLE_ADMIN'";

        List<Map<String, Object>> master = jdbcTemplate.queryForList(sqlQuery);

        model.addAttribute("tattoos", tattoo);

        model.addAttribute("masters", master);

        return "tattoo_zapis";
    }

    public User findUserByFullName(String lastname, String firstname, String patronymic) {
        String query = "SELECT * FROM users WHERE lastname = ? AND firstname = ? AND patronymic = ?";

        try {
            return jdbcTemplate.queryForObject(query, new Object[]{lastname, firstname, patronymic}, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            // Обработка ошибки или возврат null, если пользователя не найдено
            return null;
        }
    }

    public User findUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try {
            return jdbcTemplate.queryForObject(query, new Object[]{username}, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            // Обработка ошибки или возврат null, если пользователя не найдено
            return null;
        }
    }

    public TattooCatalog findTattoo(String name) {
        String query = "SELECT * FROM tattoo_catalog WHERE name = ?";

        try {
            return jdbcTemplate.queryForObject(query, new Object[]{name}, new BeanPropertyRowMapper<>(TattooCatalog.class));
        } catch (Exception e) {
            // Обработка ошибки или возврат null, если пользователя не найдено
            return null;
        }
    }

    @PostMapping("/client_menu/successfulRecord")
    public String successfulRecord(@RequestParam("date") LocalDateTime dateTime,
                                   @RequestParam("tattooName") String tattooName,
                                   @RequestParam("master_name") String masterName,
                                   @RequestParam("place") String place,
                                    Authentication authentication) {

        //   ПРОПИСАТЬ ПОЛУЧЕНИЕ ДАННЫХ И ЗАНЕСЕНИЕ В БД

        Records records = new Records();
        records.setDateTime(dateTime);
        records.setPlace(place);

        String[] substrings = masterName.split("\\s+");

        // Преобразуем массив в список
        List<String> substringList = Arrays.asList(substrings);


        User master = findUserByFullName(substringList.get(0), substringList.get(1), substringList.get(2));

        User client = findUserByUsername(authentication.getName());

        substrings = tattooName.split("\\s+");
        substringList = Arrays.asList(substrings);

        TattooCatalog tattooCatalog = findTattoo(substringList.get(0));


        records.setMaster(master);
        records.setUser(client);
        records.setTattooCatalog(tattooCatalog);


        // ТУТ КОСТЫЛЬ НА УВЕЛИЧЕНИЕ ID authority НА 1 (А-ЛЯ serial)

        String QUERY = "SELECT max(id) FROM record";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
        ) {
            while(rs.next()){
                    Integer maxId = rs.getInt(1);
                    if (maxId != null) {
                        records.setId(maxId + 1);
                    } else {
                        // Обработка случая, когда значение в колонке 'max(id)' равно null
                        records.setId(0);
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        addRecordToDB(records);



        return "client_menu";
    }




    @PostMapping("/client_menu/tattoo_tattoo_care_products")
    public String tattooCareProducts(Model model) {
        return "redirect:/client_menu/temp_tattoo_tattoo_care_products";
    }

    @GetMapping("client_menu/temp_tattoo_tattoo_care_products")
    public String getAllProducts(Model model) {
        String sqlQuery = "SELECT name, price FROM care_production";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sqlQuery);

        model.addAttribute("careProductions", resultList);
        return "tattoo_care_products";
    }


    @PostMapping("/admin_form")
    public String adminFormPost(Map<String, Object> model) {
        return "admin_form";
    }

    @PostMapping("/admin_form/profile")
    public String adminProfile(Model model) {
        return "redirect:/admin_form/temp_profile";
    }

    @GetMapping("/admin_form/temp_profile")
    public String getAdminProfile(Model model, Authentication authentication) {

        User user = userService.getUserByUserName(authentication.getName());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("firstname", user.getFirstname());
        model.addAttribute("lastname", user.getLastname());
        model.addAttribute("patronymic", user.getPatronymic());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("url", user.getUrl());

        return "adminProfile"; // This is the Thymeleaf template name
    }

    @PostMapping("/admin_form/view_records")
    public String adminViewRecords(Model model) {
        return "redirect:/admin_form/temp_admin_view_records";
    }

    @GetMapping("/admin_form/temp_admin_view_records")
    public String getAdminRecords(Model model, Authentication authentication) {
        String sqlQuery = "SELECT tc.url  as \"url\", tc.name as \"name\", r.date_time as \"date\", tc.price as \"price\", " +
                "u.lastname  as \"lastname\", u.firstname  as \"firstname\", u.patronymic  as \"patronymic\", " +
                "r.place as \"place\" FROM record r " +
                "join tattoo_catalog tc on r.tattoo_catalog_id = tc.id " +
                "join users u on r.username_client = u.username " +
                "join users m on r.username_master = m.username " +
                "where r.username_master = '" + authentication.getName() + "'";

        List<Map<String, Object>> records = jdbcTemplate.queryForList(sqlQuery);


        model.addAttribute("records", records);

        //model.addAttribute("masters", master);

        return "adminRecords";
    }

}
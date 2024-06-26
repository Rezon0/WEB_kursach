package com.example.russionTest.Services;

import com.example.russionTest.domain.User;
import com.example.russionTest.domain.VaccinationAndCertificate;
import com.example.russionTest.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUserName(String username) {
        User user = userRepository.findByusername(username);
        if (user == null) throw  new UsernameNotFoundException(username);
        return user;
    }

//    public User getUserByUserName(String username) {
//        User user = userRepository.findByusername(username);
//        if (user == null) throw  new UsernameNotFoundException(username);
//        return user;
//    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public String getRealNameByUsername(String username) {
        User user = userRepository.findByusername(username);
        return (user != null) ? user.getUsername() : "";
    }

    public String getAuthorityByusername(String username) {
        User user = userRepository.findByusername(username);
        return (user != null && user.getAuthority() != null) ? user.getAuthority().getAuthority() : null;
    }
    public void encode(String username) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.findByusername(username);
        String encr = passwordEncoder.encode(user.getPassword());
        user.setPassword(encr);
        userRepository.save(user);
    }

    public User getByLastnameAndAndFirstnameAndPatronymicAndBirthdayAndSchoolClassName(String lastname, String firstname, String patronymic, Date birthday, String schoolClassName) {
        return userRepository.findByLastnameAndAndFirstnameAndPatronymicAndBirthdayAndSchoolClassName(lastname, firstname, patronymic, birthday, schoolClassName);
    }
}

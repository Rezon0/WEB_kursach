package com.example.russionTest.Services;

import com.example.russionTest.domain.TattooCatalog;
import com.example.russionTest.domain.User;
import com.example.russionTest.repos.TattooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TattooService {
    @Autowired
    private TattooRepository tattooRepository;
    public List<TattooCatalog> getAllTattoo() {
        return tattooRepository.findAll();
    }

    public TattooCatalog getUserByName(String name) {
        TattooCatalog tattoo = tattooRepository.findByname(name);
        if (tattoo == null) throw  new UsernameNotFoundException(name);
        return tattoo;
    }
}

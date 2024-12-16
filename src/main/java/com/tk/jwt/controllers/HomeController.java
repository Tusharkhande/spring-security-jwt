package com.tk.jwt.controllers;

import com.tk.jwt.Repository.UserRepo;
import com.tk.jwt.dto.AuthRequest;
import com.tk.jwt.entity.User;
import com.tk.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth")
    public String authenticate(@RequestBody AuthRequest request){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(request.getUserName());
        }else{
            throw new UsernameNotFoundException("User not found");
        }
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id){
        return userRepo.findById(id);
    }

    @PostMapping("/users/add")
    public String addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "User added";
    }

    @DeleteMapping("/users/remove/{id}")
    public String removeUser(@PathVariable int id){
        User user = userRepo.findById(id);
        userRepo.delete(user);
        return "User removed";
    }
}

package hello.controller;

import hello.entity.LoginResult;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User loggedInUser = userService.getUserByUserName(authentication == null ? null : authentication.getName());

        if (loggedInUser == null) {
            return LoginResult.failure("No Login");
        }
        else {
            return LoginResult.success("Logged in", loggedInUser);
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public LoginResult register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        if (username == null || password == null) {
            return LoginResult.failure("Username and password are null");
        }
        if (username.length() < 1 || username.length() > 15) {
            return LoginResult.failure("Invalid username");
        }
        if (password.length() < 1 || password.length() > 16) {
            return LoginResult.failure("Invalid password");
        }

        try {
            userService.register(username, password);
        }
        catch (DuplicateKeyException e) {
            return LoginResult.failure("User already exists");
        }

        return LoginResult.success("Register successfully", null);
    }


    @PostMapping("/auth/login")
    @ResponseBody
    public LoginResult login(@RequestBody Map<String, Object> request) {
        String userName = request.get("username").toString();
        String password = request.get("password").toString();

        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(userName);
        }
        catch (UsernameNotFoundException e) {
            return LoginResult.failure("No such user");
        }

        // Get token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(token);

            return LoginResult.success("Login successfully",null);
        }
        catch (BadCredentialsException e) {
            return LoginResult.failure("Incorrect password");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedInUser = userService.getUserByUserName(userName);

        if (loggedInUser == null) {
            return LoginResult.failure("No login");
        }
        else {
            SecurityContextHolder.clearContext();

            return LoginResult.success("Logout successfully", null);
        }
    }
}

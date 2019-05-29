package hello.controller;

import hello.entity.User;
import hello.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedInUser = userService.getUserByUserName(userName);

        if (loggedInUser == null) {
            return new Result("Fail", "No Login", false);
        }
        else {
            return new Result("OK", null, true, loggedInUser);
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        if (username == null || password == null) {
            return new Result("Fail", "Username and password are null", false);
        }
        if (username.length() < 1 || username.length() > 15) {
            return new Result("Fail", "Invalid username", false);
        }
        if (password.length() < 1 || password.length() > 16) {
            return new Result("Fail", "Invalid password", false);
        }

        User user = userService.getUserByUserName(username);
        if (user == null) {
            userService.register(username, password);
            return new Result("OK", "Register successfully", true);
        }
        else {
            return new Result("Fail", "User already exists", false);
        }
    }


    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, Object> request) {
        String userName = request.get("username").toString();
        String password = request.get("password").toString();

        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(userName);
        }
        catch (UsernameNotFoundException e) {
            return new Result("fail", "No such user", false);
        }

        // Get token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(token);

            return new Result("OK", "Login successfully", true, userService.getUserByUserName(userName));
        }
        catch (BadCredentialsException e) {
            e.printStackTrace();
            return new Result("Not OK", "Incorrect password", false);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedInUser = userService.getUserByUserName(userName);

        if (loggedInUser == null) {
            return new Result("Fail", "No Login", false);
        }
        else {
            SecurityContextHolder.clearContext();

            return new Result("OK", "Logout successfully", true, loggedInUser);
        }
    }

    private static class Result {
        String status;
        String msg;
        boolean isLogin;
        Object data;

        public Result(String status, String msg, boolean isLogin) {
            this.status = status;
            this.msg = msg;
            this.isLogin = isLogin;
        }

        public Result(String status, String msg, boolean isLogin, Object data) {
            this(status, msg, isLogin);
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public Object getData() {
            return data;
        }
    }
}

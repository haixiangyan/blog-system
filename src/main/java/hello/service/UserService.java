package hello.service;

import hello.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Map<String, User> users = new ConcurrentHashMap<>();

    public UserService(Map<String, User> users) {
        this.users = users;
    }

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        register("admin", "123");
    }

    public void register(String userName, String password) {
        users.put(userName, new User(
                1, userName, bCryptPasswordEncoder.encode(password)
        ));
    }

    public User getUserByUserName(String userName) {
        return users.get(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if user exist
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException(username + " not found");
        }

        // Check if password is correct
        User user = users.get(username);

        return new org.springframework.security.core.userdetails.User(username, user.getEncodedPassword(), Collections.emptyList());
    }
}

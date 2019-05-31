package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks
    UserService userService;

    @Test
    public void testRegister() {
        // 给定一个条件
        when(mockEncoder.encode("myPassword")).thenReturn("myEncodedPassword");
        // 调用 userService
        userService.register("myUser", "myPassword");
        // 判断是否真的传入了 "myUser" 和 "myEncodedPassword"
        verify(mockMapper).register("myUser", "myEncodedPassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUserName("myUser");

        verify(mockMapper).findUserByUsername("myUser");
    }

    @Test
    public void throwExceptionWhenUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("myUser"));
    }

    @Test
    public void returnUserDetailsWhenUserFound() {
        when(mockMapper.findUserByUsername("myUser"))
                .thenReturn(new User(123, "myUser", "myEncodedPassword"));

        UserDetails userDetails = userService.loadUserByUsername("myUser");

        assertEquals(userDetails.getUsername(), "myUser");
        assertEquals(userDetails.getPassword(), "myEncodedPassword");
    }
}
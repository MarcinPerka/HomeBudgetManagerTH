package com.archu.homebudgetmanager;


import com.archu.homebudgetmanager.exception.UserAlreadyExistAuthenticationException;
import com.archu.homebudgetmanager.model.Role;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.repository.RoleRepository;
import com.archu.homebudgetmanager.repository.UserRepository;
import com.archu.homebudgetmanager.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user1, user2;

    @Before
    public void setUp() {
        user1 = new User();
        user1.setUsername("test");
        user1.setPassword("test");
        user1.setEmail("test@gmail.com");
        user1.setEnabled(true);
        user1.setExpired(false);
        user1.setCredentialsExpired(false);
        user1.setLocked(false);
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setCode("ROLE_USER");
        role.setLabel("User");
        roles.add(role);
        user1.setRoles(roles);
        user2 = new User("test2", "test", "test2@gmail.com");
        ReflectionTestUtils.setField(user1, "id", 1L);
        ReflectionTestUtils.setField(user2, "id", 2L);
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        User found = userService.getUserById(user1.getId());
        assertThat(found).isEqualTo(user1);
        verify(userRepository).findById(anyLong());
    }

    @Test(expected = NullPointerException.class)
    public void testGetUserByIdWhenUserNotFound() {
        when(userRepository.findById(user1.getId())).thenReturn(null);
        User found = userService.getUserById(user1.getId());
        assertNull(found);
        verify(userRepository).findById(anyLong());
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2));

        when(userRepository.findAll()).thenReturn(users);
        List<User> found = userService.getAllUsers();
        assertThat(found).isEqualTo(users);
        verify(userRepository).findAll();
    }

    @Test
    public void testCreateUser() throws UserAlreadyExistAuthenticationException {
        doAnswer((i) -> {
            System.out.println("Created");
            return null;
        }).when(userRepository).save(any(User.class));
        userService.createUser(user1);
        verify(userRepository).save(any(User.class));

    }

    @Test
    public void testDeleteUserById() {
        doAnswer((i) -> {
            System.out.println("Deleted");
            return "null";
        }).when(userRepository).deleteById(anyLong());
        userService.deleteUserById(anyLong());
        verify(userRepository).deleteById(anyLong());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User("admin", "admin", "admin@tmqi.com");
        ReflectionTestUtils.setField(updatedUser, "id", 1L);

        when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        doAnswer((i) -> {
            System.out.println("Updated");
            return null;
        }).when(userRepository).save(any(User.class));
        userService.updateUser(updatedUser, user1.getId());
        verify(userRepository).findById(anyLong());
        verify(userRepository).save(any(User.class));
    }

    @Test(expected = Exception.class)
    public void testUpdateUser_when_UserNotExists() throws Exception {
        User updatedUser = new User("admin", "admin", "admin@tmqi.com");
        ReflectionTestUtils.setField(updatedUser, "id", 1L);

        when(userRepository.findById(user1.getId())).thenReturn(null);
        userService.updateUser(updatedUser, user1.getId());
        verify(userRepository).findById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }
}

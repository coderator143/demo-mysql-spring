package com.example.paytm.inpg.testing;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.repositories.UserRepository;
import com.example.paytm.inpg.services.dataservice.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebMockUserTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public List<User> getTestList() {
        User user1 = new User(1, "user1", "s", "t", "email1",
                "a1", "a2", 9089, false);
        User user2 = new User(2, "user2", "p", "q", "email2",
                "a3", "a4", 90891, false);
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        return userList;
    }

    @Test
    public void getAllUsersTest() {
        List<User> userList = getTestList();
        when(userRepository.findAll()).thenReturn(userList.stream().collect(Collectors.toList()));
        assertEquals(2, userService.listAll().size());
    }

    @Test
    public void getByUserNameTest() {
        String username = "user2";
        User user1 = new User(1, "user1", "s", "t", "email1",
                "a1", "a2", 9089, false);
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        when(userRepository.findByUsername(username)).thenReturn(userList.
                stream().collect(Collectors.toList()));
        assertEquals("email1", userService.findbyUserName(username).get(0).getEmailid());
    }

    @Test
    public void getByEmailTest() {
        String emailID = "email1";
        List<User> userList = getTestList();
        when(userRepository.findByEmailid(emailID)).thenReturn(userList.stream().collect(Collectors.toList()));
        assertEquals(emailID, userService.findByEmailID(emailID).get(0).getEmailid());
    }
}

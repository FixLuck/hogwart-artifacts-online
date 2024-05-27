package com.fixluck.hogwartartifactsonline.hogwartuser;

import com.fixluck.hogwartartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<HogwartsUser> hogwartsUsers;

    @BeforeEach
    void setUp() {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("tom");
        u2.setPassword("qwerty");
        u2.setEnabled(false);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("eric");
        u3.setPassword("654321");
        u3.setEnabled(true);
        u3.setRoles("user");

        this.hogwartsUsers = new ArrayList<>();
        hogwartsUsers.add(u1);
        hogwartsUsers.add(u2);
        hogwartsUsers.add(u3);


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        //Given. Arrange inputs and targets. Define the behavior of Mock object
        given(this.userRepository.findAll()).willReturn(this.hogwartsUsers);

        //When. Act on the target behavior. Act steps should cover the method
        List<HogwartsUser> actualUsers = this.userService.findAll();

        //Then. Assert expected outcomes
        assertThat(actualUsers.size()).isEqualTo(this.hogwartsUsers.size());

        //Verify the method is called exactly once.
        verify(this.userRepository, times(1)).findAll();

    }

    @Test
    void testFindByIdSuccess() {
        //Given
        HogwartsUser u = new HogwartsUser();
        u.setId(2);
        u.setUsername("tom");
        u.setPassword("qwerty");
        u.setEnabled(false);
        u.setRoles("user");

        given(this.userRepository.findById(2)).willReturn(Optional.of(u));

        //When
        HogwartsUser returnedUser = this.userService.findById(2);

        //Then
        assertThat(returnedUser.getId()).isEqualTo(u.getId());
        assertThat(returnedUser.getUsername()).isEqualTo(u.getUsername());
        assertThat(returnedUser.getRoles()).isEqualTo(u.getRoles());

        //Verify
        verify(this.userRepository, times(1)).findById(2);

    }

    @Test
    void testFindByIdNotFound() {
        //Given
        given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //When
        Throwable throwable = catchThrowable(() -> {
            HogwartsUser user = this.userService.findById(1);
        });

        //Then
        assertThat(throwable)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1 :(");

        verify(this.userRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSuccess() {
        //Given
        HogwartsUser newUser = new HogwartsUser();
        newUser.setUsername("lily");
        newUser.setPassword("123456");
        newUser.setEnabled(true);
        newUser.setRoles("user");

        given(this.userRepository.save(newUser)).willReturn(newUser);

        //When
        HogwartsUser returnedUser = this.userService.save(newUser);

        //Then
        assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(newUser.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());

        verify(this.userRepository, times(1)).save(newUser);

    }

    @Test
    void testUpdateSuccess() {
        //Given
        HogwartsUser oldUser = new HogwartsUser();
        oldUser.setId(1);
        oldUser.setUsername("john");
        oldUser.setPassword("123456");
        oldUser.setEnabled(true);
        oldUser.setRoles("admin user");

        HogwartsUser update = new HogwartsUser();
        update.setUsername("john - updated");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(oldUser));
        given(this.userRepository.save(oldUser)).willReturn(oldUser);

        //When
        HogwartsUser updatedUser = this.userService.update(1, update);

        //Then
        assertThat(updatedUser.getId()).isEqualTo(1);
        assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
        assertThat(updatedUser.getPassword()).isEqualTo(update.getPassword());

        verify(this.userRepository, times(1)).findById(1);
        verify(this.userRepository, times(1)).save(oldUser);


    }

    @Test
    void testUpdateWithIdNotFound() {
        HogwartsUser update = new HogwartsUser();
        update.setUsername("john - updated");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());


        //When
        Throwable throwable = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.update(1, update);
        });

        //Then
        assertThat(throwable)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1 :(");

        verify(this.userRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        HogwartsUser user = new HogwartsUser();
        user.setUsername("john - updated");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(this.userRepository).deleteById(1);

        //When
        this.userService.delete(1);

        //Then
        verify(this.userRepository, times(1)).deleteById(1);

    }

    @Test
    void testDeleteNotFound() {
        //given
        given(this.userRepository.findById(1)).willReturn(Optional.empty());

        //When
        Throwable throwable = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.delete(1);
        });

        assertThat(throwable)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1 :(");

        verify(this.userRepository, times(1)).findById(1);
    }
}
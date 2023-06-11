package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.controller.AuthController;
import com.project.linguaitalianaua.model.Notification;
import com.project.linguaitalianaua.model.User;
import com.project.linguaitalianaua.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@WebMvcTest(AuthController.class)
public class AuthControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private NotificationService notificationService;

    @Mock
    private Authentication authentication;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp() {
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username("existingUser")
                .password(new BCryptPasswordEncoder().encode("password"))
                .authorities(new ArrayList<>())
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername(Mockito.anyString())).thenAnswer((Answer<?>) invocation -> {
            Object[] args = invocation.getArguments();
            if ("existingUser".equals(args[0])) {
                return user;
            }
            throw new UsernameNotFoundException("User not found");
        });
    }

    @Test
    public void testUserRegistrationValidation() throws Exception {

        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("password");

        when(userService.findByUsername("existingUser")).thenReturn(existingUser);
        when(userService.findByEmail("existing@example.com")).thenReturn(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "existingUser")
                        .param("email", "test@example.com")
                        .param("password", "pass")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "username"))
                .andExpect(content().string(containsString("An account already exists for this username.")));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "newUser")
                        .param("email", "existing@example.com")
                        .param("password", "password")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(content().string(containsString("An account already exists for this email.")));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "newUser")
                        .param("email", "test@example.com")
                        .param("password", "short")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(content().string(containsString("Password must be at least 8 characters long.")));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "newUser")
                        .param("email", "test@example.com")
                        .param("password", "password")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).save(any(User.class));
        verify(notificationService, times(7)).update(any(Notification.class));

    }

    @Test
    public void testUserLogin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "invalidUser")
                        .param("password", "password")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "validUser")
                        .param("password", "incorrectPassword")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));

        mockMvc.perform(formLogin().user("existingUser").password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testUserLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                        .with(SecurityMockMvcRequestPostProcessors.user("newUser")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));

        authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

}

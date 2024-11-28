package io.github.oosquare.neocourse;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import io.github.oosquare.neocourse.ui.view.login.LoginView;
import io.github.oosquare.neocourse.application.security.Roles;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        this.setLoginView(http, LoginView.class);
    }

    @Bean
    @Profile("development")
    public UserDetailsService users() {
        // The default password for all users is "password" when in development profile
        var student = User.builder()
            .username("student")
            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles(Roles.STUDENT)
            .build();
        var teacher = User.builder()
            .username("teacher")
            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles(Roles.TEACHER)
            .build();
        var administrator = User.builder()
            .username("administrator")
            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles(Roles.ADMINISTRATOR)
            .build();
        return new InMemoryUserDetailsManager(student, teacher, administrator);
    }
}

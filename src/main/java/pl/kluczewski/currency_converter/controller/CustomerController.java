package pl.kluczewski.currency_converter.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.kluczewski.currency_converter.model.CustomerDto;
import pl.kluczewski.currency_converter.model.entity.Customer;
import pl.kluczewski.currency_converter.service.CustomerService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class CustomerController {

    private final CustomerService userService;
    private final ModelMapper mapper;

    @PutMapping()
    public CustomerDto updateUser(@RequestBody CustomerDto user) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomerDto userUpdated = userService.update(email, mapper.map(user, Customer.class));
        return userService.update(email, mapper.map(userUpdated, Customer.class));
    }

    @DeleteMapping()
    public void deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteByEmail(email);
    }
}

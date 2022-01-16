package pl.kluczewski.currency_converter.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.kluczewski.currency_converter.model.CustomerDto;
import pl.kluczewski.currency_converter.model.entity.Customer;
import pl.kluczewski.currency_converter.service.CustomerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper mapper;

    @GetMapping
    public CustomerDto getCustomerData() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return mapper.map(customerService.findByEmail(email), CustomerDto.class);
    }

    @PutMapping()
    public CustomerDto updateUser(@RequestBody CustomerDto user) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomerDto userUpdated = customerService.update(email, mapper.map(user, Customer.class));
        return customerService.update(email, mapper.map(userUpdated, Customer.class));
    }

    @DeleteMapping()
    public void deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        customerService.deleteByEmail(email);
    }
}

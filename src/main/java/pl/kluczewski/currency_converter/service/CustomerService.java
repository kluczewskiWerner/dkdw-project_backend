package pl.kluczewski.currency_converter.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kluczewski.currency_converter.model.CustomerDto;
import pl.kluczewski.currency_converter.model.entity.Customer;
import pl.kluczewski.currency_converter.repository.CustomerRepository;
import org.modelmapper.ModelMapper;

@Service
@AllArgsConstructor
public class CustomerService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void singUpUser(Customer user) {
        boolean userExist = customerRepository.findByEmail(user.getUsername())
                .isPresent();

        if(userExist) {
            throw new IllegalStateException("Email taken");
        }

        String encodedPassWord = bCryptPasswordEncoder
                .encode(user.getPassword());

        user.setPassword(encodedPassWord);

        customerRepository.save(user);

        //TODO: send confirmation token
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow();
    }

    public CustomerDto update(String email, Customer user) {
        Customer userToUpdate = findByEmail(email);
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        return mapper.map(customerRepository.save(userToUpdate), CustomerDto.class);
    }

    public void deleteByEmail(String email) {
        Long id = customerRepository.findByEmail(email).orElseThrow().getId();
        if (customerRepository.existsById(id))
            customerRepository.deleteById(id);
    }
}

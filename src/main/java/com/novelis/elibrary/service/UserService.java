package com.novelis.elibrary.service;

import com.novelis.elibrary.dto.user.UserResponse;
import com.novelis.elibrary.entity.User;
import com.novelis.elibrary.exception.NotFoundException;
import com.novelis.elibrary.repository.LoanRepository;
import com.novelis.elibrary.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoanRepository loanRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loanRepository = loanRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public User create(User user) {
        // here you can add validation / defaults / business rules
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(Long id, User updated) {
        User existing = findById(id);
        existing.setFullName(updated.getFullName());
        existing.setEmail(updated.getEmail());
        return userRepository.save(existing);
    }

    public void delete(Long id) {
        User existing = findById(id);
        userRepository.delete(existing);
    }

    public UserResponse getUserResponseByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        int loanCount = loanRepository.countByUserId(user.getId());

        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setMembershipDate(user.getMembershipDate());
        dto.setLoanCount(loanCount);

        return dto;
    }
}

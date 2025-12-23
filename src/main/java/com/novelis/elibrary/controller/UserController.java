package com.novelis.elibrary.controller;

import com.novelis.elibrary.dto.user.UserRequest;
import com.novelis.elibrary.dto.user.UserResponse;
import com.novelis.elibrary.entity.User;
import com.novelis.elibrary.mapper.UserMapper;
import com.novelis.elibrary.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@CrossOrigin
public class UserController {
    private  final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper){
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();              // sub
        return userService.getUserResponseByEmail(email);
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userMapper.toResponse(userService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRequest userRequest){
        User user = userMapper.toEntity(userRequest);
        return userMapper.toResponse(userService.create(user));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest ){
        User exists = userService.findById(id);
        userMapper.updateEntityFromRequest(userRequest, exists);
        User updated = userService.update(id, exists);
        return userMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.delete(id);
    }
}

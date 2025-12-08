package com.novelis.elibrary.mapper;

import com.novelis.elibrary.dto.user.UserRequest;
import com.novelis.elibrary.dto.user.UserResponse;
import com.novelis.elibrary.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest userRequest){
        User user = new User();
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
        return user;
    }

    public void updateEntityFromRequest(UserRequest userRequest, User user){
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setMembershipDate(user.getMembershipDate());
        response.setLoanCount(
                user.getLoans() != null ? user.getLoans().size() : 0
        );
        return response;
    }
}

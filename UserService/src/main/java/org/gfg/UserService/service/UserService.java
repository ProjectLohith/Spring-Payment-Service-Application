package org.gfg.UserService.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.gfg.UserService.constants.UserCreationTopicConstants;
import org.gfg.UserService.dto.CreateUserRequest;
import org.gfg.UserService.enums.UserType;
import org.gfg.UserService.mapper.UserMapper;
import org.gfg.UserService.model.User;
import org.gfg.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.gfg.UserService.constants.KafkaConstants.USER_CREATION_TOPIC;
import static org.gfg.UserService.constants.UserCreationTopicConstants.EMAIL;
import static org.gfg.UserService.constants.UserCreationTopicConstants.NAME;
import static org.gfg.UserService.constants.UserCreationTopicConstants.PHONENO;
import static org.gfg.UserService.constants.UserCreationTopicConstants.USERID;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper; //gson

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNo(phoneNo);
        if (user == null) {
            throw new UsernameNotFoundException("User does not exist");
        }
        return user;
    }

    public User createUser(CreateUserRequest userRequest) {
        User user = UserMapper.mapToUser(userRequest);
        user.setUserType(UserType.USER);
        user.setAuthorities("USER");
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        log.info("User created: {}", user);
        userRepository.save(user);

        log.info("User saved: {}", user);

        //Publish the data to Kafka

        //notification service -> username, email
        //wallet service -> phoneno, userstatus

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(EMAIL, user.getEmail());
        objectNode.put(PHONENO, user.getPhoneNo());
        objectNode.put(NAME, user.getName());
        objectNode.put(USERID, user.getId());

        String kafkaMessage = objectNode.toString();
        kafkaTemplate.send(USER_CREATION_TOPIC, kafkaMessage);

        log.info("Message published to Kafka: {}", kafkaMessage);

        return user;
    }

    public User getUserByPhoneNo(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo);
    }
}

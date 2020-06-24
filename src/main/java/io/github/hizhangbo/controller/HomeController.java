package io.github.hizhangbo.controller;

import io.github.hizhangbo.model.UserProto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping(value = "/user", produces = "application/x-protobuf;charset=UTF-8")
    public UserProto.User user() {
        return UserProto.User.newBuilder()
                .setFirstName("密尔沃基")
                .setLastName("雄鹿")
                .setEmailAddress("hizhang")
//                .setSkills(0, UserProto.User.Skill.JAVA)
                .build();
    }
}

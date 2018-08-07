package com.example.demo.controller;

import com.example.demo.model.MsgResp;
import com.example.demo.service.UserService;
import com.example.demo.shiro.expception.UnauthorizedException;
import com.example.demo.shiro.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/tokens")
public class TokenController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public MsgResp getToken(@RequestParam("user_name") String userName,
                            @RequestParam("password") String password){
        if(!userService.checkUser(userName, password)){
            return new MsgResp(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), "password or username error");
        }
        return new MsgResp(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), JWTUtil.sign(userName, password));
    }
}

package com.example.demo.controller;

import com.example.demo.model.MsgResp;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/resources")
public class ResourceController {

    @RequiresAuthentication
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MsgResp getResource(){
        return new MsgResp(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "get resource");
    }

    @RequiresRoles("student")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public MsgResp postResource(){
        return new MsgResp(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "post resource");
    }

    @RequiresPermissions(logical = Logical.OR, value = {"delete"})
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public MsgResp deleteResource(){
        return new MsgResp(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "delete resource");
    }
}

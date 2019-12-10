package com.yz.meson.controller;

import com.yz.meson.model.NamespaceInfoModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class NamespaceControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @Before
    public void before(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void list() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/namespace/list");
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    public void insert() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/namespace/save")
                .param("cmd","./demo-meson/start.sh")
                .param("image","10.200.46.238:5000/meson:1.0.1")
                .param("name","meson")
                .param("optUser","shanchong");
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    public void get() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/namespace/get/{0}", 1);
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    public void update() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/namespace/save")
                        .param("id","1")
                        .param("cmd","./demo-meson/start.sh")
                        .param("image","10.200.46.238:5000/meson:1.0.1")
                        .param("name","meson")
                        .param("optUser","shanchong");
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    public void delete() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/namespace/delete/{0}", 2L);
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }
}

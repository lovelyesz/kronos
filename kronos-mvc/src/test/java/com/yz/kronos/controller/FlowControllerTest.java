package com.yz.kronos.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FlowControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void list() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/flow/list");
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString(Charset.forName("UTF-8")));
    }

    @Test
    public void insert() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/flow/save")
                .param("flowName", "测试工作流A")
                .param("namespaceId","1")
                .param("cron","0 0/10 * * * ?")
                .param("status","0");
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    public void update() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/flow/save")
                .param("id","1")
                .param("flowName", "测试工作流A")
                .param("namespaceId","1")
                .param("cron","0 0/10 * * * ?")
                .param("status","0");
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    public void delete() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/flow/delete/{1}", 1);
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    public void get() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/flow/get/{1}", 1L);
        final MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }




}

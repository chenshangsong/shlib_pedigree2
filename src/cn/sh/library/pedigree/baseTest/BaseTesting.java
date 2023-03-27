package cn.sh.library.pedigree.baseTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 测试基类
 * Created by Yi on 2014/10/29 0029.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("web")
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:pedigree-context.xml"}),
        @ContextConfiguration(locations = {"classpath:pedigree-servlet.xml"})
})
public class BaseTesting {
    @Resource
    protected WebApplicationContext wac;
    @Resource
    protected BeanFactory beanFactory;
    @Resource
    protected MockServletContext servletContext;
    @Resource
    protected MockHttpSession session;
    @Resource
    protected MockHttpServletRequest request;
//    @Resource
//    protected MockHttpServletResponse response;
//    @Resource
//    protected ServletWebRequest webRequest;
    @Resource
    protected Environment env;

    protected MockMvc mockMvc;

    @Resource
    protected ApplicationEventPublisher publisher;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


}

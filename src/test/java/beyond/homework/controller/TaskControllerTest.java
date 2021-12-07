package beyond.homework.controller;

import beyond.homework.ControllerTestHelper;
import beyond.homework.model.dto.TaskRequestDTO;
import beyond.homework.model.dto.TaskResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) /* Resetting database before each tests */
@ActiveProfiles("test")
public class TaskControllerTest extends ControllerTestHelper
{
    @Override
    @Before
    public void setUp()
    {
        super.setUp();
    }

    @Test
    public void createTask() throws Exception
    {
        String url = "/api/user/1/task";

        TaskRequestDTO taskRequestDTO = TaskRequestDTO.builder()
                .name("newTask")
                .description("newTask description")
                .dateTime("2021-12-06 17:46:00")
                .build();

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(super.mapToJson(taskRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse()
                .getStatus();

        assertEquals(200, status);

        TaskResponseDTO task = super.mapFromJson(mvcResult.getResponse()
                .getContentAsString(), TaskResponseDTO.class);

        assertEquals(new Long(3), task.getId());
        assertEquals("newTask", task.getName());
        assertEquals("newTask description", task.getDescription());
        assertEquals("pending", task.getStatus());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-12-06 17:46:00"), task.getDateTime());
    }

    @Test
    public void updateTask() throws Exception
    {
        String url = "/api/user/1/task/1";

        TaskRequestDTO taskRequestDTO = TaskRequestDTO.builder()
                .description("newTask updated description")
                .build();

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .content(super.mapToJson(taskRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse()
                .getStatus();

        assertEquals(200, status);

        TaskResponseDTO task = super.mapFromJson(mvcResult.getResponse()
                .getContentAsString(), TaskResponseDTO.class);

        assertEquals(new Long(1), task.getId());
        assertEquals("task", task.getName());
        assertEquals("newTask updated description", task.getDescription());
    }

    @Test
    public void deleteTask() throws Exception
    {
        String url = "/api/user/1/task/2";

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse()
                .getStatus();

        assertEquals(200, status);

        String url2 = "/api/user/1/task";
        MvcResult mvcResult2 = this.mockMvc.perform(MockMvcRequestBuilders.get(url2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status2 = mvcResult.getResponse()
                .getStatus();

        assertEquals(200, status2);

        String content = mvcResult2.getResponse()
                .getContentAsString();

        TaskResponseDTO[] taskList = super.mapFromJson(content, TaskResponseDTO[].class);

        assertEquals(1, taskList.length);
    }

    @Test
    public void getTask() throws Exception
    {
        String url = "/api/user/1/task/1";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse()
                .getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse()
                .getContentAsString();
        TaskResponseDTO task = super.mapFromJson(content, TaskResponseDTO.class);

        assertEquals(new Long(1), task.getId());
        assertEquals("task", task.getName());
        assertEquals("task desc", task.getDescription());
    }

    @Test
    public void getAllTasks() throws Exception
    {
        String url = "/api/user/1/task";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse()
                .getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse()
                .getContentAsString();
        TaskResponseDTO[] taskList = super.mapFromJson(content, TaskResponseDTO[].class);

        assertEquals(2, taskList.length);

        assertEquals(new Long(1), taskList[0].getId());
        assertEquals("task", taskList[0].getName());
        assertEquals("task desc", taskList[0].getDescription());

        assertEquals(new Long(2), taskList[1].getId());
        assertEquals("Login screen bug", taskList[1].getName());
        assertEquals("The Login button on the login screen is not working.", taskList[1].getDescription());
        assertEquals("done", taskList[1].getStatus());
    }
}
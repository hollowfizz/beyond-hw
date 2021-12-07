package beyond.homework.controller;

import beyond.homework.ControllerTestHelper;
import beyond.homework.model.dto.UserRequestDTO;
import beyond.homework.model.dto.UserResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) /* Resetting database before each tests */
@ActiveProfiles("test")
public class UserControllerTest extends ControllerTestHelper
{
    @Override
    @Before
    public void setUp()
    {
        super.setUp();
    }

    @Test
    public void createUser() throws Exception
    {
        String url = "/api/user";

        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .username("newuser")
                .firstName("Big")
                .lastName("Mac")
                .build();

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(super.mapToJson(userRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse()
                .getStatus();

        assertEquals(200, status);

        UserRequestDTO user = super.mapFromJson(mvcResult.getResponse()
                .getContentAsString(), UserRequestDTO.class);

        assertEquals(new Long(3), user.getId());
        assertEquals("newuser", user.getUsername());
        assertEquals("Big", user.getFirstName());
        assertEquals("Mac", user.getLastName());
    }

    @Test
    public void updateUser() throws Exception
    {
        String url = "/api/user/1";

        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .firstName("Jones")
                .build();

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .content(super.mapToJson(userRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse()
                .getStatus();

        assertEquals(200, status);

        UserRequestDTO user = super.mapFromJson(mvcResult.getResponse()
                .getContentAsString(), UserRequestDTO.class);

        assertEquals(new Long(1), user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("Jones", user.getFirstName());
        assertEquals("Gerendas", user.getLastName());
    }

    @Test
    public void getUser() throws Exception
    {
        String url = "/api/user/1";

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse()
                .getStatus();

        assertEquals(200, status);

        String content = mvcResult.getResponse()
                .getContentAsString();

        UserResponseDTO user = super.mapFromJson(content, UserResponseDTO.class);

        assertEquals(new Long(1), user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("Daniel", user.getFirstName());
        assertEquals("Gerendas", user.getLastName());
    }

    @Test
    public void getAllUsers() throws Exception
    {
        String url = "/api/user";

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse()
                .getStatus();

        assertEquals(200, status);

        String content = mvcResult.getResponse()
                .getContentAsString();

        UserResponseDTO[] userList = super.mapFromJson(content, UserResponseDTO[].class);

        assertEquals(new Long(1), userList[0].getId());
        assertEquals("admin", userList[0].getUsername());
        assertEquals("Daniel", userList[0].getFirstName());
        assertEquals("Gerendas", userList[0].getLastName());

        assertEquals(new Long(2), userList[1].getId());
        assertEquals("jsmith", userList[1].getUsername());
        assertEquals("John", userList[1].getFirstName());
        assertEquals("Smith", userList[1].getLastName());

    }
}
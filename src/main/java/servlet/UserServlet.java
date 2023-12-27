package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserDto;
import jakarta.servlet.annotation.WebServlet;
import service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserServiceImpl;
import util.ServletUtilsReadJson;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "UserServlet", value = "/application/users/*")
public class UserServlet extends HttpServlet {
    private final UserService service = new UserServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String CONTENT_TYPE = "application/json";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE);

        if (pathInfo == null) {
            List<UserDto> allUsers = service.getAllUsers();
            String json = mapper.writeValueAsString(allUsers);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);

            try {
                long userId = Long.parseLong(idString);
                UserDto user = service.getUserById(userId);

                if (user.getId() == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter()
                            .write(mapper.writeValueAsString(HttpServletResponse.SC_NOT_FOUND +
                                    ": There is no user with this ID"));
                    return;
                }
                String json = mapper.writeValueAsString(user);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);
            } catch (NumberFormatException e) {
                String json = mapper.writeValueAsString(HttpServletResponse.SC_BAD_REQUEST + ": This Invalid ID");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(json);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.getWriter().write(mapper.writeValueAsString(
                    HttpServletResponse.SC_BAD_REQUEST + ": check your input, path not found"));

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserDto userDtoFromJson = mapper.readValue(ServletUtilsReadJson.readJson(req.getReader()), UserDto.class);

        UserDto userDto = service.saveUser(userDtoFromJson);

        String json = mapper.writeValueAsString(userDto);
        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.getWriter().write(mapper.writeValueAsString(
                    HttpServletResponse.SC_BAD_REQUEST + ": path not found"));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserDto userDtoFromJson = mapper.readValue(ServletUtilsReadJson.readJson(req.getReader()), UserDto.class);

        UserDto userDto = service.updateUser(userDtoFromJson);

        String json = mapper.writeValueAsString(userDto);
        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE);
        if (pathInfo != null) {
            String idString = pathInfo.substring(1);

            try {
                long userId = Long.parseLong(idString);
                boolean b = service.deleteUser(userId);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(mapper.writeValueAsString(HttpServletResponse.SC_OK + ": data deleted - " + b));
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(mapper.writeValueAsString(
                        HttpServletResponse.SC_BAD_REQUEST + ": check your input, path must contain just number"));
            }
        }
    }
}
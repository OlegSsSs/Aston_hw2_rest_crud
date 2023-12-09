package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDto;
import dto.OrderProductDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.OrderService;
import service.OrderServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderServlet", value = "/application/orders/*")
public class OrderServlet extends HttpServlet {
    private final OrderService service = new OrderServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String CONTENT_TYPE = "application/json";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE);

        if (pathInfo == null) {
            List<OrderDto> allOrders = service.getAllOrder();
            String json = mapper.writeValueAsString(allOrders);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);

            try {
                long orderId = Long.parseLong(idString);
                OrderDto order = service.getOrderById(orderId);

                if (order.getId() == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter()
                            .write(mapper.writeValueAsString(HttpServletResponse.SC_NOT_FOUND +
                                    ": There is no order with this ID"));
                    return;
                }
                String json = mapper.writeValueAsString(order);
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
        OrderProductDto orderProductDto = mapper.readValue(readJson(req.getReader()), OrderProductDto.class);
        OrderDto orderDto = service.saveOrder(orderProductDto.getOrderDto(), orderProductDto.getProductId());

        String json = mapper.writeValueAsString(orderDto);

        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(json);
    }

    private String readJson(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine())!= null) {
            sb.append(line);
        }
        return sb.toString();
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
        OrderDto orderDtoFromJson = mapper.readValue(readJson(req.getReader()), OrderDto.class);
        OrderDto orderDto = service.updateOrder(orderDtoFromJson);

        String json = mapper.writeValueAsString(orderDto);

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
                long orderId = Long.parseLong(idString);
                boolean b = service.deleteOrder(orderId);

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

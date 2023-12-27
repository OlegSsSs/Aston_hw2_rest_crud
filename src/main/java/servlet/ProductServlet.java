package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ProductDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ProductService;
import service.ProductServiceImpl;
import util.ServletUtilsReadJson;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", value = "/application/products/*")
public class ProductServlet extends HttpServlet {
    private final ProductService service = new ProductServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String CONTENT_TYPE = "application/json";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE);

        if (pathInfo == null) {
            List<ProductDto> allProducts = service.getAllProduct();
            String json = mapper.writeValueAsString(allProducts);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);

            try {
                long productId = Long.parseLong(idString);
                ProductDto product = service.getProductById(productId);

                if (product.getId() == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter()
                            .write(mapper.writeValueAsString(HttpServletResponse.SC_NOT_FOUND +
                                    ": There is no product with this ID"));
                    return;
                }
                String json = mapper.writeValueAsString(product);
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
        ProductDto productDtoFromJson = mapper.readValue(ServletUtilsReadJson.readJson(req.getReader()), ProductDto.class);
        ProductDto productDto = service.saveProduct(productDtoFromJson);
        String json = mapper.writeValueAsString(productDto);

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
        ProductDto productDtoFromJson = mapper.readValue(ServletUtilsReadJson.readJson(req.getReader()), ProductDto.class);
        ProductDto productDto = service.updateProduct(productDtoFromJson);
        String json = mapper.writeValueAsString(productDto);

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
                long productId = Long.parseLong(idString);
                boolean b = service.deleteProduct(productId);

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
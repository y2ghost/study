package study.ywork.servlet.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;

@WebServlet(name = "SessionServlet", urlPatterns = { "/products", "/viewProductDetails", "/addToCart", "/viewCart" })
public class SessionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CART_ATTRIBUTE = "cart";
    private final List<Product> products = new ArrayList<>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);

    @Override
    public void init() {
        products.add(new Product(1, "小米52寸高清电视", "低成本的高清电视，来自著名的电视制造商", new BigDecimal("159.95")));
        products.add(new Product(2, "华为蓝光播放器", "高品质时尚的蓝光播放器", new BigDecimal("99.95")));
        products.add(new Product(3, "博世音响系统", "5喇叭高保真系统", new BigDecimal("129.95")));
        products.add(new Product(4, "苹果iPod播放器", "一个可以播放多种格式的iPod插件", new BigDecimal("39.95")));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        if (uri.endsWith("/products")) {
            sendProductList(response);
        } else if (uri.endsWith("/viewProductDetails")) {
            sendProductDetails(request, response);
        } else if (uri.endsWith("viewCart")) {
            showCart(request, response);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = 0;
        int quantity = 0;

        try {
            productId = Integer.parseInt(request.getParameter("id"));
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
        }

        Product product = getProduct(productId);
        if (product != null && quantity >= 0) {
            ShoppingItem shoppingItem = new ShoppingItem(product, quantity);
            HttpSession session = request.getSession();
            List<ShoppingItem> cart = (List<ShoppingItem>) session.getAttribute(CART_ATTRIBUTE);

            if (cart == null) {
                cart = new ArrayList<>();
                session.setAttribute(CART_ATTRIBUTE, cart);
            }

            cart.add(shoppingItem);
        }

        sendProductList(response);
    }

    private void sendProductList(HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.println("<html><head><title>产品信息</title>" + "</head><body><h2>产品信息</h2>");
        writer.println("<ul>");

        products.forEach((product) -> {
            writer.println("<li>" + product.getName() + "(" + currencyFormat.format(product.getPrice()) + ") ("
                + "<a href='viewProductDetails?id=" + product.getId() + "'>产品详细信息</a>)");
        });

        writer.println("</ul>");
        writer.println("<a href='viewCart'>查看购物车</a>");
        writer.println("</body></html>");

    }

    private Product getProduct(int productId) {
        Product rc = null;
        for (Product product : products) {
            if (product.getId() == productId) {
                rc = product;
                break;
            }
        }

        return rc;
    }

    private void sendProductDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        int productId = 0;

        try {
            productId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
        }

        Product product = getProduct(productId);
        if (product != null) {
            writer.println("<html><head>" + "<title>产品详细信息</title></head>" + "<body><h2>产品详细信息</h2>"
                + "<form method='post' action='addToCart'>");
            writer.println("<input type='hidden' name='id' " + "value='" + productId + "'/>");
            writer.println("<table>");
            writer.println("<tr><td>产品名称:</td><td>" + product.getName() + "</td></tr>");
            writer.println("<tr><td>产品描述:</td><td>" + product.getDescription() + "</td></tr>");
            writer.println("<tr>" + "<tr>" + "<td><input name='quantity'/></td>"
                + "<td><input type='submit' value='购买'/>" + "</td>" + "</tr>");
            writer.println("<tr><td colspan='2'>" + "<a href='products'>产品列表</a>" + "</td></tr>");
            writer.println("</table>");
            writer.println("</form></body>");
        } else {
            writer.println("无产品销售");
        }
    }

    @SuppressWarnings("unchecked")
    private void showCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.println("<html><head><title>购物车</title></head>");
        writer.println("<body><a href='products'>产品列表</a>");
        HttpSession session = request.getSession();
        List<ShoppingItem> cart = (List<ShoppingItem>) session.getAttribute(CART_ATTRIBUTE);

        if (cart != null) {
            writer.println("<table>");
            writer.println("<tr><td style='width:150px'>数量" + "</td>" + "<td style='width:150px'>Product</td>"
                + "<td style='width:150px'>价格</td>" + "<td>总金额</td></tr>");

            BigDecimal total = BigDecimal.ZERO;
            for (ShoppingItem shoppingItem : cart) {
                Product product = shoppingItem.getProduct();
                int quantity = shoppingItem.getQuantity();

                if (quantity != 0) {
                    BigDecimal price = product.getPrice();
                    writer.println("<tr>");
                    writer.println("<td>" + quantity + "</td>");
                    writer.println("<td>" + product.getName() + "</td>");
                    writer.println("<td>" + currencyFormat.format(price) + "</td>");
                    BigDecimal subtotal = price.multiply(new BigDecimal(quantity));
                    writer.println("<td>" + currencyFormat.format(subtotal) + "</td>");
                    total = total.add(subtotal);
                    writer.println("</tr>");
                }
            }

            writer.println("<tr><td colspan='4' " + "style='text-align:right'>" + "总共:" + currencyFormat.format(total)
                + "</td></tr>");
            writer.println("</table>");
        }

        writer.println("</table></body></html>");
    }

    @SuppressWarnings("unused")
    private static class Product {
        private int id;
        private String name;
        private String description;
        private BigDecimal price;

        public Product(int id, String name, String description, BigDecimal price) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }

    @SuppressWarnings("unused")
    private static class ShoppingItem {
        private Product product;
        private int quantity;

        public ShoppingItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
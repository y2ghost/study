package study.ywork.servlet.cookie;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;

/**
 * 隐藏域技术 本例子线程不安全，仅仅用来示例 缺点，不适合跨越多个页面，比网址重写稍微好点
 */
@WebServlet(name = "CustomerServlet", urlPatterns = { "/customer", "/editCustomer", "/updateCustomer" })
public class HiddenFields extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final List<Customer> customers = new ArrayList<>();

    @Override
    public void init() {
        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("小豆");
        customer1.setCity("孙家庄");
        customers.add(customer1);

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("小宇");
        customer2.setCity("孙家庄");
        customers.add(customer2);
    }

    private void sendCustomerList(HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.println("<html><head><title>顾客</title></head>" + "<body><h2>顾客信息</h2>");
        writer.println("<ul>");

        customers.forEach((customer) -> {
            writer.println("<li>" + customer.getName() + "(" + customer.getCity() + ") (" + "<a href='editCustomer?id="
                + customer.getId() + "'>编辑</a>)");
        });
        writer.println("</ul>");
        writer.println("</body></html>");
    }

    private Customer getCustomer(int customerId) {
        Customer rc = null;
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                rc = customer;
                break;
            }
        }

        return rc;
    }

    private void sendEditCustomerForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        int customerId = 0;

        try {
            customerId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
        }

        Customer customer = getCustomer(customerId);
        if (customer != null) {
            writer.println("<html><head>" + "<title>编辑顾客</title></head>" + "<body><h2>编辑顾客信息</h2>"
                + "<form method='post' " + "action='updateCustomer'>");
            writer.println("<input type='hidden' name='id' value='" + customerId + "'/>");
            writer.println("<table>");
            writer.println("<tr><td>名称:</td><td>" + "<input name='name' value='"
                + customer.getName().replace("'", "&#39;") + "'/></td></tr>");
            writer.println("<tr><td>城市:</td><td>" + "<input name='city' value='"
                + customer.getCity().replace("'", "&#39;") + "'/></td></tr>");
            writer.println("<tr>" + "<td colspan='2' style='text-align:right'>"
                + "<input type='submit' value='更新'/></td>" + "</tr>");
            writer.println("<tr><td colspan='2'>" + "<a href='customer'>顾客列表</a>" + "</td></tr>");
            writer.println("</table>");
            writer.println("</form></body>");
        } else {
            writer.println("顾客信息没找到");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        if (uri.endsWith("/customer")) {
            sendCustomerList(response);
        } else if (uri.endsWith("/editCustomer")) {
            sendEditCustomerForm(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        int customerId = 0;

        try {
            customerId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
        }

        Customer customer = getCustomer(customerId);
        if (customer != null) {
            customer.setName(request.getParameter("name"));
            customer.setCity(request.getParameter("city"));
        }

        sendCustomerList(response);
    }

    private static class Customer {
        private int id;
        private String name;
        private String city;

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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
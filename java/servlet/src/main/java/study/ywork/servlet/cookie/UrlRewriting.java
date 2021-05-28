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
 *
 * 网址重写技术，适合不多的网页，不常用了 缺点，不适合跨越多个页面
 */
@WebServlet(name = "Top10Servlet", urlPatterns = { "/top10" })
public class UrlRewriting extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final List<String> londonAttractions = new ArrayList<>(10);
    private static final List<String> parisAttractions = new ArrayList<>(10);
    private static final String LONDON = "london";
    private static final String PARIS = "paris";
    private static final String CITY = "city";
    private static final String PAGE = "page";

    @Override
    public void init() {
        /* 伦敦景点 */
        londonAttractions.add("白金汉宫");
        londonAttractions.add("伦敦眼");
        londonAttractions.add("英国博物馆");
        londonAttractions.add("国家画廊");
        londonAttractions.add("大本钟");
        londonAttractions.add("伦敦塔");
        londonAttractions.add("自然历史博物馆");
        londonAttractions.add("金丝雀码头");
        londonAttractions.add("2012奥林匹克公园");
        londonAttractions.add("圣保罗大教堂");

        /* 巴黎景点 */
        parisAttractions.add("埃菲尔铁塔");
        parisAttractions.add("巴黎圣母院");
        parisAttractions.add("卢浮宫");
        parisAttractions.add("香榭丽舍大街");
        parisAttractions.add("凯旋门");
        parisAttractions.add("圣礼拜堂");
        parisAttractions.add("荣军院");
        parisAttractions.add("奥赛博物馆");
        parisAttractions.add("蒙马特");
        parisAttractions.add("圣心大教堂");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String city = request.getParameter(CITY);
        if (LONDON.equals(city) || PARIS.equals(city)) {
            showAttractions(request, response, city);
        } else {
            showMainPage(request, response);
        }
    }

    private void showMainPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.print("<html><head>" + "<title>十大旅游景点</title>" + "</head><body>" + "请你选择城市:"
            + "<br/><a href='?city=london'>伦敦</a>" + "<br/><a href='?city=paris'>巴黎</a>" + "</body></html>");
    }

    private void showAttractions(HttpServletRequest request, HttpServletResponse response, String city)
        throws IOException {
        int page = 1;
        String pageParameter = request.getParameter(PAGE);
        if (pageParameter != null) {
            try {
                page = Integer.parseInt(pageParameter);
            } catch (NumberFormatException e) {
                page = 1;
            }

            if (page > 2) {
                page = 1;
            }
        }

        List<String> attractions = null;
        if (LONDON.equals(city)) {
            attractions = londonAttractions;
        } else if (PARIS.equals(city)) {
            attractions = parisAttractions;
        }

        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.println("<html><head>" + "<title>十大旅游景点</title>" + "</head><body>");
        writer.println("<a href='top10'>选择城市</a> ");
        writer.println("<hr/>第" + page + "页<hr/>");

        int start = page * 5 - 5;
        String attraction = null;

        for (int i = start; i < start + 5; i++) {
            attraction = attractions.get(i);
            if (null != attraction) {
                writer.println(attraction + "<br/>");
            }
        }

        writer.print("<hr style='color:blue'/>" + "<a href='?city=" + city + "&page=1'>页面 1</a>");
        writer.println("&nbsp; <a href='?city=" + city + "&page=2'>页面 2</a>");
        writer.println("</body></html>");
    }
}
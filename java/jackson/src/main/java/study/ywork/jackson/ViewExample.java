package study.ywork.jackson;

import java.util.Date;
import java.io.IOException;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/*
 * 视图就是定义的空类和接口，用于分组使用
 */
public class ViewExample {
    public static void main(String[] args) throws IOException {
        quickContactView();
        summaryView();
        summaryViewDeserialization();
    }

    public static void quickContactView() throws IOException {
        Customer customer = new Customer();
        customer.setName("yy");
        customer.setAddress("湖南岳阳");
        customer.setPhone("111-111-111");
        customer.setCellPhone("222-222-222");
        customer.setCustomerSince(Date.from(ZonedDateTime.now().minusYears(8).toInstant()));
        customer.setEmailAddress("Jackson@example.com");

        System.out.println("\n-- quickContactView --");
        System.out.println("-- 序列化之前 --");
        System.out.println(customer);

        JsonMapper om = JsonMapper.builder().disable(MapperFeature.DEFAULT_VIEW_INCLUSION).build();
        String jsonString = om.writerWithView(Views.QuickContact.class).writeValueAsString(customer);
        System.out.println("-- 序列化之后 --");
        System.out.println(jsonString);
    }

    public static void summaryView() throws IOException {
        Customer customer = new Customer();
        customer.setName("yy");
        customer.setAddress("湖南岳阳");
        customer.setPhone("111-111-111");
        customer.setCellPhone("222-222-222");
        customer.setCustomerSince(Date.from(ZonedDateTime.now().minusYears(8).toInstant()));
        customer.setEmailAddress("Jackson@example.com");

        System.out.println("\n-- summaryView --");
        System.out.println("-- 序列化之前 --");
        System.out.println(customer);

        JsonMapper om = JsonMapper.builder().disable(MapperFeature.DEFAULT_VIEW_INCLUSION).build();
        String jsonString = om.writerWithView(Views.SummaryView.class).writeValueAsString(customer);
        System.out.println("-- 序列化之后 --");
        System.out.println(jsonString);
    }

    private static void summaryViewDeserialization() throws IOException {
        String jsonString = "{\"name\":\"Emily\",\"address\":\"湖南岳阳\","
                + "\"phone\":\"111-111-111\",\"cellPhone\":\"222-222-222\","
                + "\"emailAddress\":\"emily@example.com\",\"customerSince\":\"2012/08/23\"}";
        System.out.println("\n-- summaryViewDeserialization --");
        System.out.println("-- 反序列化之前 --");
        System.out.println(jsonString);

        JsonMapper om = JsonMapper.builder().disable(MapperFeature.DEFAULT_VIEW_INCLUSION).build();
        Customer customer = om.readerWithView(Views.SummaryView.class).forType(Customer.class).readValue(jsonString);
        System.out.println("-- 反序列化之后 --");
        System.out.println(customer);
    }

    private static class Views {
        public interface QuickContact {
        }

        public interface SummaryView {
        }
    }

    /*
     * 类上使用@JsonView注解则表示默认的视图，除非每个属性各自定义覆盖 此处并非必要，学习示例而已
     */
    @SuppressWarnings("unused")
    @JsonView(Views.SummaryView.class)
    private static class Customer {
        @JsonView({ Views.SummaryView.class, Views.QuickContact.class })
        private String name;
        @JsonView(Views.SummaryView.class)
        private String address;
        private String phone;
        @JsonView(Views.QuickContact.class)
        private String cellPhone;
        private String emailAddress;
        @JsonView(Views.SummaryView.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Shanghai")
        private Date customerSince;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCellPhone() {
            return cellPhone;
        }

        public void setCellPhone(String cellPhone) {
            this.cellPhone = cellPhone;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public Date getCustomerSince() {
            return customerSince;
        }

        public void setCustomerSince(Date customerSince) {
            this.customerSince = customerSince;
        }

        @Override
        public String toString() {
            return "Customer [name=" + name + ", address=" + address + ", phone=" + phone + ", cellPhone=" + cellPhone
                    + ", emailAddress=" + emailAddress + ", customerSince=" + customerSince + "]";
        }
    }
}

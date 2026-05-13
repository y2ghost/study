package study.ywork.selenium.rest;

import java.util.Map;

public class HttpBinGet {
    public Map<String, String> args;
    public Map<String, String> headers;
    public String origin;
    public String url;

    public Map<String, String> getArgs() {
        return args;
    }

    void setArgs(Map<String, String> args) {
        this.args = args;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getOrigin() {
        return origin;
    }

    void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }
}

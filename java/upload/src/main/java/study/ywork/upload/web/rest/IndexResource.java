package study.ywork.upload.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexResource {
    @GetMapping("/")
    public String gotoPage() {
        return "index";
    }

    @GetMapping("/uploadFile")
    public String gotoFilePage() {
        return "upload";
    }

    @GetMapping("/oss/upload")
    public String gotoOssPage() {
        return "ossUpload";
    }
}

package com.replace.main;

/**
 * @ProjectName: replace-project
 * @Package: com.replace.main
 * @ClassName: Config
 * @Author: janck
 * @Description:
 * @Date: 2024/4/12 15:22
 * @Version: 1.0
 */
public class Config {
    // dns 查询
    public static final String SEARCH_URL = "https://myssl.com/api/v1/tools/dns_query?qtype=1&host={}&qmode=-1";
    // 需要修改的域名数组
    public static final String[] HOST_LIST = {
            "github.com" ,
            "google.com",
            "github.global.ssl.fastly.net",
            "assets-cdn.github.com",
            "translate.googleapis.com",
            "translate.google.com",
            "translate-pa.googleapis.com",
            "collector.github.com",
            "raw.githubusercontent.com",
            "avatars0.githubusercontent.com"
    };

    // window config
    public static String WI_HOST_URL = "C:\\Windows\\System32\\drivers\\etc\\hosts";
    // Linux config
    public static String LINUX_URL = "/etc/hosts";

    /**
     * hosts文件内容前缀
     */
    public static String HOST_PREFIX = "# github speedup start";
    /**
     * hosts文件内容后缀
     */
    public static String HOST_SUFFIX = "# github speedup end";

}

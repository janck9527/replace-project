package com.replace.main.utils.file;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 文件处理工具类
 *
 * @author janck
 */
public class FileUtils {
    /**
     * 字符常量：斜杠 {@code '/'}
     */
    public static final char SLASH = '/';

    /**
     * 字符常量：反斜杠 {@code '\\'}
     */
    public static final char BACKSLASH = '\\';

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 创建不存在的文件
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static boolean createExistsFile(String filename) throws IOException {
        File file = new File(filename);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        return file.createNewFile();
    }


    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource) {
        // 禁止目录上跳级别
        if (StringUtils.contains(resource, "..")) {
            return false;
        }

        // 检查允许下载的文件规则
        if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource))) {
            return true;
        }

        // 不在允许下载的文件规则
        return false;
    }


    /**
     * 返回文件名
     *
     * @param filePath 文件
     * @return 文件名
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }


    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * 文件数据写入（如果文件夹和文件不存在，则先创建，再写入）
     *
     * @param filePath
     * @param content
     * @param flag     true:如果文件存在且存在内容，则内容换行追加；false:如果文件存在且存在内容，则内容替换
     */
    public static String fileLinesWrite(String filePath, String content, boolean flag) {
        String filedo = "write";
        FileWriter fw = null;
        try {
            File file = new File(filePath);
            //如果文件夹不存在，则创建文件夹
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {//如果文件不存在，则创建文件,写入第一行内容
                file.createNewFile();
                fw = new FileWriter(file);
                filedo = "create";
            } else {//如果文件存在,则追加或替换内容
                fw = new FileWriter(file, flag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filedo;
    }

    /**
     * 写文件
     *
     * @param ins
     * @param out
     */
    public static void writeIntoOut(InputStream ins, OutputStream out) {
        byte[] bb = new byte[10 * 1024];
        try {
            int cnt = ins.read(bb);
            while (cnt > 0) {
                out.write(bb, 0, cnt);
                cnt = ins.read(bb);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                ins.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     *
     * @param Path
     * @return
     */
    public static String ReadFile(String Path) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    /**
     * 获取文件夹下所有文件的名称 + 模糊查询（当不需要模糊查询时，queryStr传空或null即可）
     * 1.当路径不存在时，map返回retType值为1
     * 2.当路径为文件路径时，map返回retType值为2，文件名fileName值为文件名
     * 3.当路径下有文件夹时，map返回retType值为3，文件名列表fileNameList，文件夹名列表folderNameList
     *
     * @param folderPath 路径
     * @param queryStr   模糊查询字符串
     * @return
     */
    public static HashMap<String, Object> getFilesName(String folderPath, String queryStr) {
        HashMap<String, Object> map = new HashMap<>();
        List<String> fileNameList = new ArrayList<>();//文件名列表
        List<String> folderNameList = new ArrayList<>();//文件夹名列表
        File f = new File(folderPath);
        if (!f.exists()) { //路径不存在
            map.put("retType", "1");
        } else {
            boolean flag = f.isDirectory();
            if (flag == false) { //路径为文件
                map.put("retType", "2");
                map.put("fileName", f.getName());
            } else { //路径为文件夹
                map.put("retType", "3");
                File fa[] = f.listFiles();
                queryStr = queryStr == null ? "" : queryStr;//若queryStr传入为null,则替换为空（indexOf匹配值不能为null）
                for (int i = 0; i < fa.length; i++) {
                    File fs = fa[i];
                    if (fs.getName().indexOf(queryStr) != -1) {
                        if (fs.isDirectory()) {
                            folderNameList.add(fs.getName());
                        } else {
                            fileNameList.add(fs.getName());
                        }
                    }
                }
                map.put("fileNameList", fileNameList);
                map.put("folderNameList", folderNameList);
            }
        }
        return map;
    }

    /**
     * 以行为单位读取文件，读取到最后一行
     *
     * @param filePath
     * @return
     */
    public static List<String> readFileContent(String filePath) {
        BufferedReader reader = null;
        List<String> listContent = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                listContent.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return listContent;
    }

    /**
     * 以行为单位读取文件，读取到最后一行
     *
     * @param filePath
     * @return
     */
    public static String readFileContentLine(String filePath) {
        BufferedReader reader = null;
        StringBuilder listContent = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                listContent.append(tempString).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return listContent.toString();
    }

    /**
     * 以行为单位读取文件，读取到最后一行
     *
     * @param inputStream
     * @return
     */
    public static String readFileContentLine(InputStream inputStream) {
        BufferedReader reader = null;
        StringBuilder listContent = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                listContent.append(tempString).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return listContent.toString();
    }

    /**
     * 读取指定行数据 ，注意：0为开始行
     *
     * @param filePath
     * @param lineNumber
     * @return
     */
    public static String readLineContent(String filePath, int lineNumber) {
        BufferedReader reader = null;
        String lineContent = "";
        try {
            reader = new BufferedReader(new FileReader(filePath));
            int line = 0;
            while (line <= lineNumber) {
                lineContent = reader.readLine();
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return lineContent;
    }

    /**
     * 读取从beginLine到endLine数据（包含beginLine和endLine），注意：0为开始行
     *
     * @param filePath
     * @param beginLineNumber 开始行
     * @param endLineNumber   结束行
     * @return
     */
    public static List<String> readLinesContent(String filePath, int beginLineNumber, int endLineNumber) {
        List<String> listContent = new ArrayList<>();
        try {
            int count = 0;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String content = reader.readLine();
            while (content != null) {
                if (count >= beginLineNumber && count <= endLineNumber) {
                    listContent.add(content);
                }
                content = reader.readLine();
                count++;
            }
        } catch (Exception e) {
        }
        return listContent;
    }

    /**
     * 读取若干文件中所有数据
     *
     * @param listFilePath
     * @return
     */
    public static List<String> readFileContent_list(List<String> listFilePath) {
        List<String> listContent = new ArrayList<>();
        for (String filePath : listFilePath) {
            File file = new File(filePath);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    listContent.add(tempString);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        return listContent;
    }

    /**
     * 删除文件夹及其下面的子文件夹
     *
     * @param dir
     * @throws IOException
     */
    public static void deleteDir(File dir) throws IOException {
        if (dir.isFile()) {
            throw new IOException("IOException -> BadInputException: not a directory.");
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    file.delete();
                } else {
                    deleteDir(file);
                }
            }
        }
        dir.delete();
    }


    /**
     * 复制文件
     *
     * @param src
     * @param dst
     * @throws Exception
     */
    public static void copy(File src, File dst) throws Exception {
        int BUFFER_SIZE = 4096;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
        }
    }

    public static List<?> readLines(File file) {
        List<String> listContent = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                listContent.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return listContent;
    }

    public static void writeLines(File file, List<String> newLines) {
        newLines.stream().forEach(line->{
            fileLinesWrite(file.getPath(), line,true);
        });
    }

    /**
     * 获取host文件路径
     * @return
     */
    public static String getHostFile() {
        String fileName = null;
        // 判断系统
        if ("linux".equalsIgnoreCase(System.getProperty("os.name"))||
                "Mac OS X".equalsIgnoreCase(System.getProperty("os.name"))) {
            fileName = "/etc/hosts";
        } else {
            fileName = System.getenv("windir") + "\\system32\\drivers\\etc\\hosts";
        }
        return fileName;
    }

    /**
     * 根据输入IP和Domain，删除host文件中的某个host配置
     * @param ip
     * @param domain
     * @return
     */
    public synchronized static boolean deleteHost(String ip, String domain) {
        if (ip == null || ip.trim().isEmpty() || domain == null || domain.trim().isEmpty()) {
            throw new IllegalArgumentException("ERROR： ip & domain must be specified");
        }
        String splitter = " ";
        /**
         * Step1: 获取host文件
         */
        String fileName = getHostFile();
        List<?> hostFileDataLines = null;
        hostFileDataLines = FileUtils.readLines(new File(fileName));
        /**
         * Step2: 解析host文件，如果指定域名不存在，则Ignore，如果已经存在，则直接删除该行配置
         */
        List<String> newLinesList = new ArrayList<String>();
        // 标识本次文件是否有更新，比如如果指定的IP和域名已经在host文件中存在，则不用再写文件
        boolean updateFlag = false;
        for (Object line : hostFileDataLines) {
            String strLine = line.toString();
            // 将host文件中的空行或无效行，直接去掉
            if (StringUtils.isEmpty(strLine) || strLine.trim().equals("#")) {
                continue;
            }
            // 如果没有被注释掉，则
            if (!strLine.trim().startsWith("#")) {
                strLine = strLine.replaceAll("", splitter);
                int index = strLine.toLowerCase().indexOf(domain.toLowerCase());
                // 如果行字符可以匹配上指定域名，则针对该行做操作
                if (index != -1) {
                    // 匹配到相同的域名，直接将整行数据干掉
                    updateFlag = true;
                    continue;
                }
            }
            // 如果没有匹配到，直接将当前行加入代码中
            newLinesList.add(strLine);
        }
        /**
         * Step3: 将更新写入host文件中去
         */
        if (updateFlag) {
            FileUtils.writeLines(new File(fileName), newLinesList);
        }
        return true;
    }
    /**
     * 根据输入IP和Domain，更新host文件中的某个host配置
     * @param ip
     * @param domain
     * @return
     */
    public synchronized static boolean updateHost(String ip, String domain) {
        // Security.setProperty("networkaddress.cache.ttl", "0");
        // Security.setProperty("networkaddress.cache.negative.ttl", "0");
        if (ip == null || ip.trim().isEmpty() || domain == null || domain.trim().isEmpty()) {
            throw new IllegalArgumentException("ERROR： ip & domain must be specified");
        }
        String splitter = " ";
        /**
         * Step1: 获取host文件
         */
        String fileName = getHostFile();
        List<?> hostFileDataLines = null;
        hostFileDataLines = FileUtils.readLines(new File(fileName));
        /**
         * Step2: 解析host文件，如果指定域名不存在，则追加，如果已经存在，则修改IP进行保存
         */
        List<String> newLinesList = new ArrayList<String>();
        // 指定domain是否存在，如果存在，则不追加
        boolean findFlag = false;
        // 标识本次文件是否有更新，比如如果指定的IP和域名已经在host文件中存在，则不用再写文件
        boolean updateFlag = false;
        for (Object line : hostFileDataLines) {
            String strLine = line.toString();
            // 将host文件中的空行或无效行，直接去掉
            if (StringUtils.isEmpty(strLine) || strLine.trim().equals("#")) {
                continue;
            }
            if (!strLine.startsWith("#")) {
                strLine = strLine.replaceAll("", splitter);
                int index = strLine.toLowerCase().indexOf(domain.toLowerCase());
                // 如果行字符可以匹配上指定域名，则针对该行做操作
                if (index != -1) {
                    // 如果之前已经找到过一条，则说明当前line的域名已重复，
                    // 故删除当前line, 不将该条数据放到newLinesList中去
                    if (findFlag) {
                        updateFlag = true;
                        continue;
                    }
                    // 不然，则继续寻找
                    String[] array = strLine.trim().split(splitter);
                    Boolean isMatch = false;
                    for (int i = 1; i < array.length; i++) {
                        if (domain.equalsIgnoreCase(array[i]) == false) {
                            continue;
                        } else {
                            findFlag = true;
                            isMatch = true;
                            // IP相同，则不更新该条数据，直接将数据放到newLinesList中去
                            if (array[0].equals(ip) == false) {
                                // IP不同，将匹配上的domain的ip 更新成设定好的IP地址
                                StringBuilder sb = new StringBuilder();
                                sb.append(ip);
                                for (int j = 1; i < array.length; i++) {
                                    sb.append(splitter).append(array[j]);
                                }
                                strLine = sb.toString();
                                updateFlag = true;
                            }
                        }
                    }
                }
            }
            // 如果有更新，则会直接更新到strLine中去
            // 故这里直接将strLine赋值给newLinesList
            newLinesList.add(strLine);
        }
        /**
         * Step3: 如果没有任何Host域名匹配上，则追加
         */
        if (!findFlag) {
            newLinesList.add(new StringBuilder(ip).append(splitter).append(domain).toString());
        }
        /**
         * Step4: 不管三七二十一，写设定文件
         */
        if (updateFlag || !findFlag) {
            FileUtils.writeLines(new File(fileName), newLinesList);
        }
        return true;
    }
}

package com.course.jwt;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;

public class JWTServletRequestWrapper extends HttpServletRequestWrapper {


    private final byte[] body;

    public JWTServletRequestWrapper(HttpServletRequest request, String txKey, String userId, Date requestTime) throws UnsupportedEncodingException {
        super(request);

        String bodyStr = getBodyString(request);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = null;
        try {
            jsonObject = parser.parse(bodyStr).getAsJsonObject();
            jsonObject.addProperty("requsterUserId", userId);
            jsonObject.addProperty("requestTime", DateFormatUtils.format(requestTime, "yyyy-MM-dd HH:mm:ss.SSS"));
            jsonObject.addProperty("txKey", txKey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {

            if (jsonObject != null) {
                body = jsonObject.toString().getBytes("UTF-8");
            } else {
                body = bodyStr.getBytes("UTF-8");
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public String getBodyString(final ServletRequest request) {
        try {
            return inputStream2String(request.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getBodyString() {
        final InputStream inputStream = new ByteArrayInputStream(body);
        return inputStream2String(inputStream);
    }

    private String inputStream2String(InputStream inputStream) {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        };
    }

}

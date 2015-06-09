package com.example.jing.entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;

public class Common {
	
	public interface HttpEntityBuilder {
        public HttpEntity buildEntity() throws UnsupportedEncodingException;
        public HttpEntity buildEntity(ArrayList<NameValueObj> pairs) throws UnsupportedEncodingException;
    }
	
	public static class NameValueObj {
        public NameValueObj(String key, String value) {
            this.key = key;
            this.value = value;
        }
        public String key;
        public String value;
    }
}

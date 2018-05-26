package com.queue.tuna;

/**
 * Created by zhangtao on 2017/1/27.
 */
public class Innertest {
    private String name = "tom";
    class Inner{
        private String name = "inner";
        public void changeName(){
            Innertest.this.name = "Jane";
        }
    }
    public String getname(){
        return name;
    }
}



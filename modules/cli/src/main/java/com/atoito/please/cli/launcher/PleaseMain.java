package com.atoito.please.cli.launcher;




public class PleaseMain {
    
    private final static String PLEASE_APP_CLASS = "com.atoito.please.cli.launcher.PleaseApp";

    public static void main(String[] args) throws Exception {

        new ProcessBootstrap().run(PLEASE_APP_CLASS, args);

    }

}

package onethreeseven.common;

import onethreeseven.common.util.Res;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Todo: write documentation
 *
 * @author Luke Bermingham
 */
public class Main {

    public static void main(String[] args) throws IOException {


        URL file = Thread.currentThread().getContextClassLoader().getResource("GrabMe.txt");



        System.out.println(file);

    }

}

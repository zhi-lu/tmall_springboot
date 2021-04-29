package com.luzhi.tmall.util;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/21
 * 对redis运行端口6379进行查看..判断redis是否启动..
 */
public class PortUtil {

    /**
     * 此静态方法判断端口是否正在运行...
     *
     * @see #testPort(int) {@code new ServerSocket}
     * 具体实现{@link ServerSocket}
     */
    public static boolean testPort(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * 此静态方法进行具体判断redis是否启动和redis未启动作出什么响应
     *
     * @see #checkPort(int, String, boolean) 具体实现{@link JOptionPane#showConfirmDialog(Component, Object)}；
     * {@link JOptionPane#showMessageDialog(Component, Object)}
     * 详情请看
     * @see PortUtil#testPort(int)
     */
    public static void checkPort(int port, String server, boolean shutdown) {
        if (!testPort(port)) {
            if (shutdown) {
                String message = String.format("此计算机端口:%d, 服务:\"%s\"未检查到启动.%n", port, server);
                JOptionPane.showMessageDialog(null, message);
                System.exit(1);
            } else {
                String message = String.format("此计算机端口:%d, 服务:\"%s\"未检查到启动.是否继续...%n", port, server);
                if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null, message)) {
                    System.exit(1);
                }
            }
        }
    }

}

/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package dev.panda.crates.utils;

import dev.panda.crates.Crates;
import dev.panda.crates.utils.chat.ChatUtil;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerUtil {
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d_)(\\d*)(_R\\d)");
    public static final String SERVER_VERSION;
    public static final int SERVER_VERSION_INT;

    public static String getIP() {
        String ipAddress;
        try {
            String line;
            URL url = new URL("http://checkip.amazonaws.com/%22");
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            ipAddress = builder.toString();
        } catch (UnknownHostException ex) {
            ipAddress = "NONE";
            ChatUtil.log("[PandaLicense] Problem on the page with the IPS.");
        } catch (IOException ex) {
            ipAddress = "NONE";
            ChatUtil.log("[PandaLicense] Error in check your host IP.");
        }
        return ipAddress + ":" + Crates.getInstance().getServer().getPort();
    }

    private ServerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        Matcher matcher = VERSION_PATTERN.matcher(SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1));
        SERVER_VERSION_INT = matcher.find() ? Integer.parseInt(matcher.group(2)) : -1;
    }
}


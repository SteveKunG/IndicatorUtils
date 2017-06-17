package stevekung.mods.indicatorutils.utils;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public class AutoLogin
{
    private Map<String, AutoLoginData> autoLogin = Maps.newHashMap();

    @Nullable
    public AutoLoginData getAutoLogin(String data)
    {
        return this.autoLogin.get(data);
    }

    public AutoLoginData addAutoLogin(String serverIP, String command, String value, String username)
    {
        AutoLoginData login = this.getAutoLogin(username + serverIP);

        if (login != null)
        {
            throw new IllegalArgumentException("An auto login data already set for Username: " + username + "!");
        }
        else
        {
            login = new AutoLoginData(serverIP, command, value, username);
            this.autoLogin.put(username + serverIP, login);
            return login;
        }
    }

    public void removeAutoLogin(String data)
    {
        this.autoLogin.remove(data);
    }

    public Collection<AutoLoginData> getAutoLoginList()
    {
        return this.autoLogin.values();
    }

    public static class AutoLoginData
    {
        private String serverIP;
        private String command;
        private String value;
        private String username;

        public AutoLoginData(String serverIP, String command, String value, String username)
        {
            this.serverIP = serverIP;
            this.command = command;
            this.value = value;
            this.username = username;
        }

        public String getServerIP()
        {
            return this.serverIP;
        }

        public String getCommand()
        {
            return this.command;
        }

        public String getValue()
        {
            return this.value;
        }

        public String getUsername()
        {
            return this.username;
        }
    }
}
/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

public class AutoLogin
{
    private Map<String, AutoLoginData> autoLogin = Maps.<String, AutoLoginData>newHashMap();

    public AutoLoginData getAutoLogin(String username)
    {
        return this.autoLogin.get(username);
    }

    public AutoLoginData addAutoLogin(String serverIP, String command, String value, String username)
    {
        AutoLoginData login = this.getAutoLogin(username);

        if (login != null)
        {
            throw new IllegalArgumentException("An auto login data already set for Username: " + username + "!");
        }
        else
        {
            login = new AutoLoginData(serverIP, command, value, username);
            this.autoLogin.put(username, login);
            return login;
        }
    }

    public void removeAutoLogin(String username)
    {
        this.autoLogin.remove(username);
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
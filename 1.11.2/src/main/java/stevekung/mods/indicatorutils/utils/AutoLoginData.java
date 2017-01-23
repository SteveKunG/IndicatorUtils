/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public class AutoLoginData
{
    private Map<String, AutoLogin> autoLogin = Maps.<String, AutoLogin>newHashMap();

    @Nullable
    public AutoLogin getAutoLogin(String serverIP)
    {
        return this.autoLogin.get(serverIP);
    }

    public AutoLogin addAutoLogin(String serverIP, String command, String value)
    {
        AutoLogin login = this.getAutoLogin(serverIP);

        if (login != null)
        {
            throw new IllegalArgumentException("An auto login data already set for Server: " + serverIP + "!");
        }
        else
        {
            login = new AutoLogin(serverIP, command, value);
            this.autoLogin.put(serverIP, login);
            return login;
        }
    }

    public void removeAutoLogin(String serverIP)
    {
        this.autoLogin.remove(serverIP);
    }

    public Collection<AutoLogin> getAutoLogin()
    {
        return this.autoLogin.values();
    }

    public static class AutoLogin
    {
        private String serverIP;
        private String command;
        private String value;

        public AutoLogin(String serverIP, String command, String value)
        {
            this.serverIP = serverIP;
            this.command = command;
            this.value = value;
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
    }
}
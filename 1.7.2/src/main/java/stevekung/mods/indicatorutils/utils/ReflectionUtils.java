/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.lang.reflect.Field;

public class ReflectionUtils
{
    public static <T> void setFinal(String name, T value, Class<?> clazz, T instance)
    {
        try
        {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & -17);
            field.set(clazz.cast(instance), value);
        }
        catch (Exception e) {}
    }
}
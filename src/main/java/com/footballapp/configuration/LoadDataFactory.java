package com.footballapp.configuration;

import com.footballapp.utils.Constants;

class LoadDataFactory
{
    public static ILoadTeamsData getFileLoadClass(Constants.FileLoadMode fileLoadMode)
    {
        if ( fileLoadMode.equals(Constants.FileLoadMode.LOAD_ON_STARTUP) )
            return new FileDataload();

        return null;
    }
}

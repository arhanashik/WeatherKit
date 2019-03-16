package com.workfort.weatherkit.app.data.local.appconst;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 12/28/2018 at 4:28 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 12/28/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

public interface Const {
    interface RequestCode {
        int LOCATION = 1010;
    }

    interface Key {
    }

    interface RemoteConfig {
        String BASE_URL = "https://api.openweathermap.org/data/2.5/";
        String ACCESS_TOKEN = "your-access-token-here";
        String CONTENT_TYPE = "application/x-www-form-urlencoded";
    }
}
